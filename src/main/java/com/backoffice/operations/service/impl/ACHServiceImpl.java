package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.*;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.*;
import com.backoffice.operations.service.ACHService;
import com.backoffice.operations.service.BeneficiaryService;
import com.backoffice.operations.service.OtpService;
import com.backoffice.operations.utils.ApiCaller;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ACHServiceImpl implements ACHService {

	private static final Logger logger = LoggerFactory.getLogger(ACHServiceImpl.class);

	@Value("${external.api.ach.transfer.bank}")
	private String achBankTransfer;
	private final SequenceCounterRepository sequenceCounterRepository;

	private final OtpService otpService;

	private final CommonUtils commonUtils;

	private final TransferAccountFieldsRepository transferAccountFieldsRepository;

	private final SourceOperationRepository sourceOperationRepository;

	private final AccountCurrencyRepository accountCurrencyRepository;

	private final BeneficiaryBankRepository beneficiaryBankRepository;

	@Value("${external.api.accounts}")
	private String accountExternalAPI;

	@Qualifier("jwtAuth")
	@Autowired
	private RestTemplate jwtAuthRestTemplate;

	private final RestTemplate restTemplate;

	private final ApiCaller apiCaller;

	private final BeneficiaryService beneficiaryService;

	private final ObjectMapper objectMapper;

	private final TransactionRepository transactionRepository;

	private final BankListRepo bankListRepo;

	public ACHServiceImpl(SequenceCounterRepository sequenceCounterRepository, OtpService otpService,
						  CommonUtils commonUtils, TransferAccountFieldsRepository transferAccountFieldsRepository,
						  SourceOperationRepository sourceOperationRepository, AccountCurrencyRepository accountCurrencyRepository,
						  BeneficiaryBankRepository beneficiaryBankRepository, RestTemplate restTemplate, ApiCaller apiCaller,
						  BeneficiaryService beneficiaryService, ObjectMapper objectMapper,
						  TransactionRepository transactionRepository, BankListRepo bankListRepo) {
		this.sequenceCounterRepository = sequenceCounterRepository;
		this.otpService = otpService;
		this.commonUtils = commonUtils;
		this.transferAccountFieldsRepository = transferAccountFieldsRepository;
		this.sourceOperationRepository = sourceOperationRepository;
		this.accountCurrencyRepository = accountCurrencyRepository;
		this.beneficiaryBankRepository = beneficiaryBankRepository;
		this.restTemplate = restTemplate;
		this.apiCaller = apiCaller;
		this.beneficiaryService = beneficiaryService;
		this.objectMapper = objectMapper;
		this.transactionRepository = transactionRepository;
		this.bankListRepo = bankListRepo;
	}

	@Override
	public GenericResponseDTO<Object> transferToACHAccount(AlizzTransferRequestDto alizzTransferRequestDto) {

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		Map<String, Object> data = new HashMap<>();
		String transactionRefcode = "TRAH";
		String refId = String.format("%012d", getNextSequence());
		String txnRefId = transactionRefcode + refId;
		String trnxDate = "";
		try {
			if (Objects.nonNull(alizzTransferRequestDto) && StringUtils.hasLength(alizzTransferRequestDto.getOtp())) {
				GenericResponseDTO<Object> validationResultDTO = otpService
						.transferOTP(alizzTransferRequestDto.getUniqueKey(), alizzTransferRequestDto.getOtp());

				if (Objects.nonNull(validationResultDTO) && validationResultDTO.getStatus().equalsIgnoreCase("Failure"))
					return validationResultDTO;

				ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
				if (Objects.nonNull(response.getBody())) {

					trnxDate = commonUtils.getBankDate(alizzTransferRequestDto.getFromAccountNumber().substring(0, 3));

					AlizzTransferResponseDto alizzTransferResponseDto = new AlizzTransferResponseDto();

					HttpHeaders headers = new HttpHeaders();
					headers.setBearerAuth(response.getBody().getAccessToken());

					TransferAccountFields transferAccountFields = transferAccountFieldsRepository
							.findByTransferType("ACH");

					SourceOperation sourceOperation = sourceOperationRepository.findBySourceCode("ach");

					AccountCurrency accountCurrency = accountCurrencyRepository.findAll().get(0);

					AlizzTransferDto alizzTransferDto = new AlizzTransferDto();
					AlizzTransferDto.Header header = new AlizzTransferDto.Header();
					header.setSource_system(sourceOperation.getSourceSystem());
					header.setSource_user(alizzTransferRequestDto.getUniqueKey());
					header.setSource_operation(sourceOperation.getSourceOperation());

					AlizzTransferDto.Transaction transaction = getTransactionDetails(alizzTransferRequestDto, txnRefId,
							accountCurrency, transferAccountFields, trnxDate);

					AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount senderAccDetails = getIslamicAccount(
							alizzTransferRequestDto.getFromAccountNumber());

					if (Objects.isNull(senderAccDetails)) {
						return getErrorResponseGenericDTO(alizzTransferRequestDto, "Sender Account Invalid");
					}

					AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount receiverAccDetails = getIslamicAccount(
							alizzTransferRequestDto.getToAccountNumber());

					if (Objects.isNull(receiverAccDetails)) {
						return getErrorResponseGenericDTO(alizzTransferRequestDto, "Receiver Account Invalid");
					}

					AlizzTransferDto.Sender sender = getSenderDetails(alizzTransferRequestDto, senderAccDetails);

					AlizzTransferDto.Receiver receiver = getReceiverDetails(alizzTransferRequestDto, receiverAccDetails);

					double avlBalance = apiCaller.getAvailableBalance(alizzTransferRequestDto.getFromAccountNumber());
					if (avlBalance > alizzTransferRequestDto.getTransactionAmount()) {
						transaction.setTransactionAmount(alizzTransferRequestDto.getTransactionAmount());
					} else {
						data.put("message", "Payment failed!");
						data.put("transactionID", txnRefId);
						data.put("transactionDateTime", trnxDate);
						data.put("uniqueKey", alizzTransferRequestDto.getUniqueKey());
						data.put("status", "Failure");
						responseDTO.setStatus("Success");
						responseDTO.setMessage("Failure");
						responseDTO.setData(data);
						return responseDTO;
					}

					alizzTransferDto.setSender(sender);
					alizzTransferDto.setReceiver(receiver);
					alizzTransferDto.setHeader(header);
					alizzTransferDto.setTransaction(transaction);

					ObjectMapper objectMapper = JsonMapper.builder()
							.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
					String jsonRequestBody = objectMapper.writeValueAsString(alizzTransferDto);
					logger.info("jsonRequestBody: {}", jsonRequestBody);

					headers.setContentType(MediaType.APPLICATION_JSON);

					HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
					ResponseEntity<FundTransferResponseDto> responseEntity = restTemplate.exchange(achBankTransfer,
							HttpMethod.POST, requestEntity, FundTransferResponseDto.class);

					if (responseEntity.getStatusCode().is2xxSuccessful()) {
						Beneficiary beneficiary = beneficiaryService.addBeneficiary(alizzTransferDto.getReceiver());
						alizzTransferResponseDto.setAccountName(beneficiary.getAccountName());
						alizzTransferResponseDto.setAccountNumber(beneficiary.getAccountNumber());
						alizzTransferResponseDto.setBankName(alizzTransferRequestDto.getToBankName());
						FundTransferResponseDto fundTransferResponseDto = responseEntity.getBody();
						logger.info("responseEntity.getBody(): {}", responseEntity.getBody());

						responseDTO = getResponseDto(alizzTransferRequestDto.getUniqueKey(),
								responseEntity.getStatusCode().is2xxSuccessful(), fundTransferResponseDto, txnRefId,
								transaction.getTransactionDate());
					}
					return responseDTO;
				}
			} else {
				data.put("message", "Payment failed!");
				data.put("transactionID", txnRefId);
				data.put("transactionDateTime", trnxDate);
				data.put("uniqueKey", alizzTransferRequestDto.getUniqueKey());
				data.put("status", "Failure");
				responseDTO.setStatus("Success");
				responseDTO.setMessage("Failure");
				responseDTO.setData(data);
				return responseDTO;
			}
		} catch (Exception e) {
			logger.error("ERROR in class ACHServiceImpl method transferToACHAccount : {}", e.getMessage());
			data.put("message", "Payment failed!");
			data.put("transactionID", txnRefId);
			data.put("transactionDateTime", trnxDate);
			data.put("uniqueKey", alizzTransferRequestDto.getUniqueKey());
			data.put("status", "Failure");
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Failure");
			responseDTO.setData(data);
			return responseDTO;
		}
		return responseDTO;
	}

	public GenericResponseDTO<Object> getResponseDto(String uniqueKey, boolean isSuccessful,
			FundTransferResponseDto fundTransferResponseDto, String txnRefId, String txnDate)
			throws JsonProcessingException {

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		Map<String, Object> data = new HashMap<>();
		try {
			if (isSuccessful && Objects.nonNull(fundTransferResponseDto) && fundTransferResponseDto.isSuccess()) {

				List<FundTransferResponseDto.Fcubswarningresp> warningResponse = Objects
						.nonNull(fundTransferResponseDto.getResponse().getResult().getFcubswarningresp())
								? fundTransferResponseDto.getResponse().getResult().getFcubswarningresp()
								: new ArrayList<>();

				List<FundTransferResponseDto.Fcubserrorresp> errorResponse = Objects
						.nonNull(fundTransferResponseDto.getResponse().getResult().getFcubserrorresp())
								? fundTransferResponseDto.getResponse().getResult().getFcubserrorresp()
								: new ArrayList<>();

				Long responseTxnRefNo = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn()
						.getGrpTlr().getTxnRefNo();
				String reqExctnDt = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getPmtInf()
						.getReqdExctnDt();

				if (errorResponse.isEmpty()) {
					String warningResponseString = objectMapper
							.writeValueAsString(!warningResponse.isEmpty() ? warningResponse : "");
					Transaction transactionObj = Transaction.builder()
							.responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
							.txnStatus("Success").warningResponse(warningResponseString).txnDate(reqExctnDt)
							.uniqueKey(uniqueKey).build();

					transactionRepository.save(transactionObj);
					data.put("transactionID", txnRefId);
					data.put("uniqueKey", uniqueKey);
					data.put("transactionDateTime", reqExctnDt);
					data.put("message", "Payment successful!");
					data.put("status", "Success");
					responseDTO.setData(data);
					responseDTO.setMessage("Success");
					responseDTO.setStatus("Success");
				} else {
					String errorResponseString = objectMapper
							.writeValueAsString(!errorResponse.isEmpty() ? errorResponse : "");
					Transaction transactionObj = Transaction.builder()
							.responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
							.txnStatus("Failure").errorResponse(errorResponseString).txnDate(reqExctnDt)
							.uniqueKey(uniqueKey).build();

					transactionRepository.save(transactionObj);
					data.put("transactionID", txnRefId);
					data.put("uniqueKey", uniqueKey);
					data.put("transactionDateTime", reqExctnDt);
					data.put("message", "Payment failed!");
					data.put("status", "Failure");
//					data.put("error", errorResponse);
					responseDTO.setData(data);
					responseDTO.setMessage("Payment failed!");
					responseDTO.setStatus("Success");
				}
			} else if (isSuccessful && Objects.nonNull(fundTransferResponseDto)
					&& !fundTransferResponseDto.isSuccess()) {
				Long responseTxnRefNo = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn()
						.getGrpTlr().getTxnRefNo();
				String reqExctnDt = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getPmtInf()
						.getReqdExctnDt();

				List<FundTransferResponseDto.Fcubswarningresp> warningResponse = Objects
						.nonNull(fundTransferResponseDto.getResponse().getResult().getFcubswarningresp())
								? fundTransferResponseDto.getResponse().getResult().getFcubswarningresp()
								: new ArrayList<>();

				List<FundTransferResponseDto.Fcubserrorresp> errorResponse = Objects
						.nonNull(fundTransferResponseDto.getResponse().getResult().getFcubserrorresp())
								? fundTransferResponseDto.getResponse().getResult().getFcubserrorresp()
								: new ArrayList<>();

				String warningResponseString = objectMapper
						.writeValueAsString(!warningResponse.isEmpty() ? warningResponse : "");
				String errorResponseString = objectMapper
						.writeValueAsString(!errorResponse.isEmpty() ? errorResponse : "");
				Transaction transactionObj = Transaction.builder()
						.responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
						.txnStatus("Failure").warningResponse(warningResponseString).errorResponse(errorResponseString)
						.txnDate(reqExctnDt).uniqueKey(uniqueKey).build();

				transactionRepository.save(transactionObj);
				data.put("transactionID", txnRefId);
				data.put("uniqueKey", uniqueKey);
				data.put("transactionDateTime", reqExctnDt);
				data.put("message", "Payment failed!");
				data.put("status", "Failure");
//				data.put("error", errorResponse);
				responseDTO.setData(data);
				responseDTO.setMessage("Payment failed!");
				responseDTO.setStatus("Success");
			}
		} catch (Exception e) {
			logger.error("ERROR on getResponseDto : {}", e.getMessage());
			responseDTO.setData(new HashMap<>());
			data.put("uniqueKey", uniqueKey);
			data.put("message", "Payment failed!");
			data.put("status", "Failure");
			responseDTO.setData(data);
			responseDTO.setMessage("Payment failed!");
			responseDTO.setStatus("Failure");
		}
		return responseDTO;
	}

	private AlizzTransferDto.Receiver getReceiverDetails(AlizzTransferRequestDto alizzTransferRequestDto,
			AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount receiverAccDetails) {
		try {
			BankList banklist = bankListRepo.findByBicCode(alizzTransferRequestDto.getToBankName());
			return AlizzTransferDto.Receiver.builder().notesToReceiver("/INT/" + alizzTransferRequestDto.getNotesToReceiver())
					.accountName(alizzTransferRequestDto.getToAccountName()).accountNumber(receiverAccDetails.getAcc())
					.bankCode(Objects.nonNull(banklist) ? banklist.getBicCode() : "")
					.bankName(Objects.nonNull(banklist) ? banklist.getBankName() : "")
					.branchCode(Objects.nonNull(banklist) ? banklist.getBicCode() : "").iBanAccountNumber("")
					.bankAddress1("Muscat").bankAddress2("").bankAddress3("").bankAddress4("OM")
					.beneAddress1("").beneAddress2("").beneAddress3("").beneAddress4("OM")
					.bankCountry("Oman").build();
		} catch (Exception e) {
			logger.error("ERROR on getReceiverDetails : {}", e.getMessage());
			return AlizzTransferDto.Receiver.builder().build();
		}
	}

	private AlizzTransferDto.Sender getSenderDetails(AlizzTransferRequestDto alizzTransferRequestDto,
			AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount senderAccDetails) {
		try {
			String senderCifNo = alizzTransferRequestDto.getFromAccountNumber().substring(3, 10);
			BankList banklist = bankListRepo.findByBankName("Alizz Islamic Bank");

			AlizzTransferDto.Sender sender = AlizzTransferDto.Sender.builder().accountNumber(senderAccDetails.getAcc())
					.accountCurrency(senderAccDetails.getCcy())
					.bankCode(banklist.getBicCode())
					.branchCode(alizzTransferRequestDto.getFromAccountNumber().substring(0, 3))
					.bankName("Alizz Islamic Bank").build();

			Optional<AccountDetails> senderAccountInfo = getTokenAndApiResponse(senderCifNo);
			senderAccountInfo.ifPresent(accountInfo -> {
				if (accountInfo.getResponse() != null && accountInfo.getResponse().getPayload() != null
						&& accountInfo.getResponse().getPayload().getCustSummaryDetails() != null) {
					List<AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount> islamicAccounts = accountInfo
							.getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts();
					for (AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount islamicAccount : islamicAccounts) {
						if (islamicAccount.getAcc().equalsIgnoreCase(alizzTransferRequestDto.getFromAccountNumber())) {
							sender.setAccountName(islamicAccount.getAdesc());
							break;
						}
					}
				}
			});
			return sender;
		} catch (Exception e) {
			logger.error("ERROR on getSenderDetails : {}", e.getMessage());
			return AlizzTransferDto.Sender.builder().build();
		}
	}

	private AlizzTransferDto.Transaction getTransactionDetails(AlizzTransferRequestDto alizzTransferRequestDto,
			String txnRefId, AccountCurrency accountCurrency, TransferAccountFields transferAccountFields,
			String trnxDate) {
		try {
			return AlizzTransferDto.Transaction.builder().paymentDetails1(alizzTransferRequestDto.getNotesToReceiver())
					.paymentDetails2("").paymentDetails3("").paymentDetails4("").transactionReference(txnRefId)
					.transactionDate(trnxDate).transactionAmount(alizzTransferRequestDto.getTransactionAmount())
					.transactionPurpose(alizzTransferRequestDto.getTransactionPurpose())
					.transactionCurrency(accountCurrency.getAccountCurrency())
					.cbsModule(Objects.nonNull(transferAccountFields)
							&& Objects.nonNull(transferAccountFields.getCbsModule())
									? transferAccountFields.getCbsModule()
									: "")
					.cbsNetwork(Objects.nonNull(transferAccountFields)
							&& Objects.nonNull(transferAccountFields.getCbsNetwork())
									? transferAccountFields.getCbsNetwork()
									: "")
					.cbsProduct(Objects.nonNull(transferAccountFields)
							&& Objects.nonNull(transferAccountFields.getCbsProduct())
									? transferAccountFields.getCbsProduct()
									: "")
					.chargeType(Objects.nonNull(transferAccountFields)
							&& Objects.nonNull(transferAccountFields.getChargeType())
									? transferAccountFields.getChargeType()
									: "")
					.build();
		} catch (Exception e) {
			logger.error("ERROR on getTransactionDetails : {}", e.getMessage());
			return AlizzTransferDto.Transaction.builder().build();
		}
	}

	private AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount getIslamicAccount(String accountNumber) {
		try {
			String receiverCifNo = accountNumber.substring(3, 10);
			Optional<AccountDetails> receiverAccountInfo = getTokenAndApiResponse(receiverCifNo);
			if (receiverAccountInfo.isPresent()) {
				List<AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount> accountDetails = receiverAccountInfo
						.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts();
				return accountDetails.stream().filter(acc -> acc.getAcc().equals(accountNumber)).toList().get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("ERROR on getIslamicAccount : {}", e.getMessage());
			return null;
		}
	}

	private Optional<AccountDetails> getTokenAndApiResponse(String civilId) {
		try {
			ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
			if (Objects.nonNull(response.getBody())) {
				HttpHeaders headers = new HttpHeaders();
				headers.setBearerAuth(response.getBody().getAccessToken());
				HttpEntity<String> entity = new HttpEntity<>(headers);

				String apiUrl = accountExternalAPI + civilId;
				ResponseEntity<AccountDetails> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET,
						entity, AccountDetails.class);
				return Optional.ofNullable(responseEntity.getBody());
			}
		} catch (Exception e) {
			logger.error("ERROR on getTokenAndApiResponse : {}", e.getMessage());
		}
		return Optional.empty();
	}

	private static GenericResponseDTO<Object> getErrorResponseGenericDTO(
			AlizzTransferRequestDto alizzTransferRequestDto, String Receiver_Account_Invalid) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		Map<String, Object> data = new HashMap<>();
		data.put("uniqueKey", alizzTransferRequestDto.getUniqueKey());
		data.put("message", Receiver_Account_Invalid);
		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Failure");
		responseDTO.setData(data);
		return responseDTO;
	}

	private synchronized long getNextSequence() {
		SequenceCounter sequenceCounter = sequenceCounterRepository.findById(1L).orElseGet(() -> {
			SequenceCounter newCounter = new SequenceCounter();
			newCounter.setNextValue(0L);
			return sequenceCounterRepository.save(newCounter);
		});

		long nextValue = sequenceCounter.getNextValue() + 1;
		sequenceCounter.setNextValue(nextValue);

		sequenceCounterRepository.save(sequenceCounter);

		return nextValue;
	}
}
