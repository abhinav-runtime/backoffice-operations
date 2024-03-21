package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.*;
import com.backoffice.operations.enums.TransferType;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.*;
import com.backoffice.operations.service.AlizzTransferService;
import com.backoffice.operations.service.BeneficiaryService;
import com.backoffice.operations.service.OtpService;
import com.backoffice.operations.service.TransferLimitService;
import com.backoffice.operations.utils.ApiCaller;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
public class AlizzTransferServiceImpl implements AlizzTransferService {

	private static final Logger logger = LoggerFactory.getLogger(AlizzTransferServiceImpl.class);

	@Value("${external.api.transfer.bank}")
	private String alizzBankTransfer;

	private final CommonUtils commonUtils;

	private final RestTemplate restTemplate;

	private final BeneficiaryService beneficiaryService;

	@Value("${external.api.accounts}")
	private String accountExternalAPI;

	@Autowired
	@Qualifier("jwtAuth")
	private RestTemplate jwtAuthRestTemplate;

	private final TransferAccountFieldsRepository transferAccountFieldsRepository;

	private final SourceOperationRepository sourceOperationRepository;

	private final AccountCurrencyRepository accountCurrencyRepository;

	private final SequenceCounterRepository sequenceCounterRepository;
	private final TransactionRepository transactionRepository;

	private final BeneficiaryBankRepository beneficiaryBankRepository;

	private final ApiCaller apiCaller;

	private final ObjectMapper objectMapper;
	private final OtpRepository otpRepository;

	private final OtpService otpService;

	@Value("${external.charge.api}")
	private String chargeApi;

	@Autowired
	private TransactionCodeRepo transactionCodeRepo;

	private final ProfileRepository profileRepository;

	private final TransferLimitService transferLimitService;

	public AlizzTransferServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate,
			BeneficiaryService beneficiaryService, TransferAccountFieldsRepository transferAccountFieldsRepository,
			SourceOperationRepository sourceOperationRepository, AccountCurrencyRepository accountCurrencyRepository,
			SequenceCounterRepository sequenceCounterRepository, TransactionRepository transactionRepository,
			BeneficiaryBankRepository beneficiaryBankRepository, ApiCaller apiCaller, ObjectMapper objectMapper,
			OtpRepository otpRepository, OtpService otpService, ProfileRepository profileRepository, TransferLimitService transferLimitService) {
		this.commonUtils = commonUtils;
		this.restTemplate = restTemplate;
		this.beneficiaryService = beneficiaryService;
		this.transferAccountFieldsRepository = transferAccountFieldsRepository;
		this.sourceOperationRepository = sourceOperationRepository;
		this.accountCurrencyRepository = accountCurrencyRepository;
		this.sequenceCounterRepository = sequenceCounterRepository;
		this.transactionRepository = transactionRepository;
		this.beneficiaryBankRepository = beneficiaryBankRepository;
		this.apiCaller = apiCaller;
		this.objectMapper = objectMapper;
		this.otpRepository = otpRepository;
		this.otpService = otpService;
		this.profileRepository = profileRepository;
        this.transferLimitService = transferLimitService;
    }

	@Override
	public GenericResponseDTO<Object> transferToAlizzAccount(AlizzTransferRequestDto alizzTransferRequestDto)
			throws JsonProcessingException {

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		Map<String, Object> data = new HashMap<>();
		String transactionRefcode = "TRWA";
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
							.findByTransferType("WITHIN ALIZZ");

					SourceOperation sourceOperation = sourceOperationRepository.findBySourceCode("WithinAlizz");

					AccountCurrency accountCurrency = accountCurrencyRepository.findAll().get(0);
					BeneficiaryBank beneficiaryBank = beneficiaryBankRepository
							.findByBankName(alizzTransferRequestDto.getToBankName());

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

					AlizzTransferDto.Sender sender = getSenderDetails(alizzTransferRequestDto, senderAccDetails,
							beneficiaryBank);

					AlizzTransferDto.Receiver receiver = getReceiverDetails(alizzTransferRequestDto, receiverAccDetails,
							beneficiaryBank);

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

					GenericResponseDTO<Object> responseObject = transferLimitService.getTransferLimit(alizzTransferRequestDto.getCustomerType(),
							alizzTransferRequestDto.getUniqueKey(),alizzTransferRequestDto.getTransactionType(),alizzTransferRequestDto.getTransactionAmount());
					Object map = responseObject.getData();
					Map<String, Object> resMap = objectMapper.convertValue(map, new TypeReference<Map<String,Object>>() {
					});
					if (resMap.containsKey("isTrxnAllowed") && resMap.get("isTrxnAllowed").equals(true)) {
						ObjectMapper objectMapper = JsonMapper.builder()
								.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
						String jsonRequestBody = objectMapper.writeValueAsString(alizzTransferDto);
						logger.info("jsonRequestBody: {}", jsonRequestBody);

						headers.setContentType(MediaType.APPLICATION_JSON);

						HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
						ResponseEntity<FundTransferResponseDto> responseEntity = restTemplate.exchange(alizzBankTransfer,
								HttpMethod.POST, requestEntity, FundTransferResponseDto.class);

						if (responseEntity.getStatusCode().is2xxSuccessful()) {
							Beneficiary beneficiary = beneficiaryService.addBeneficiary(alizzTransferDto.getReceiver());
							alizzTransferResponseDto.setAccountName(beneficiary.getAccountName());
							alizzTransferResponseDto.setAccountNumber(beneficiary.getAccountNumber());
							alizzTransferResponseDto.setBankName("Alizz bank");
							FundTransferResponseDto fundTransferResponseDto = responseEntity.getBody();
							logger.info("responseEntity.getBody(): {}", responseEntity.getBody());

							responseDTO = getResponseDto(alizzTransferRequestDto.getUniqueKey(),
									responseEntity.getStatusCode().is2xxSuccessful(), fundTransferResponseDto, txnRefId,
									transaction.getTransactionDate(),alizzTransferRequestDto.getTransactionAmount(),
									alizzTransferRequestDto.getFromAccountNumber());
						}
						return responseDTO;
					}else {
						return responseObject;
					}
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
			logger.error("ERROR in class AlizzTransferServiceImpl method transferToAlizzAccount", e);
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
			FundTransferResponseDto fundTransferResponseDto, String txnRefId, String txnDate, Double transactionAmount, String accountNumber)
			throws JsonProcessingException {

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		Map<String, Object> data = new HashMap<>();

		if (isSuccessful && Objects.nonNull(fundTransferResponseDto) && fundTransferResponseDto.isSuccess()) {

			List<FundTransferResponseDto.Fcubswarningresp> warningResponse = Objects
					.nonNull(fundTransferResponseDto.getResponse().getResult().getFcubswarningresp())
							? fundTransferResponseDto.getResponse().getResult().getFcubswarningresp()
							: new ArrayList<>();

			List<FundTransferResponseDto.Fcubserrorresp> errorResponse = Objects
					.nonNull(fundTransferResponseDto.getResponse().getResult().getFcubserrorresp())
							? fundTransferResponseDto.getResponse().getResult().getFcubserrorresp()
							: new ArrayList<>();

			Long responseTxnRefNo = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getGrpTlr()
					.getTxnRefNo();
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

				transferLimitService.saveUserTrxnLimitData(uniqueKey, transactionAmount, accountNumber);
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
				data.put("error", errorResponse);
				responseDTO.setData(data);
				responseDTO.setMessage("Failure");
				responseDTO.setStatus("Success");
			}
		} else if (isSuccessful && Objects.nonNull(fundTransferResponseDto) && !fundTransferResponseDto.isSuccess()) {
			Long responseTxnRefNo = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getGrpTlr()
					.getTxnRefNo();
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
			String errorResponseString = objectMapper.writeValueAsString(!errorResponse.isEmpty() ? errorResponse : "");
			Transaction transactionObj = Transaction.builder().responseTxnReferenceId(String.valueOf(responseTxnRefNo))
					.txnReferenceId(txnRefId).txnStatus("Failure").warningResponse(warningResponseString)
					.errorResponse(errorResponseString).txnDate(reqExctnDt).uniqueKey(uniqueKey).build();

			transactionRepository.save(transactionObj);
			data.put("transactionID", txnRefId);
			data.put("uniqueKey", uniqueKey);
			data.put("transactionDateTime", reqExctnDt);
			data.put("message", "Payment failed!");
			data.put("status", "Failure");
			data.put("error", errorResponse);
			responseDTO.setData(data);
			responseDTO.setMessage("Failure");
			responseDTO.setStatus("Success");
		}
		return responseDTO;
	}

	private static AlizzTransferDto.Receiver getReceiverDetails(AlizzTransferRequestDto alizzTransferRequestDto,
			AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount receiverAccDetails,
			BeneficiaryBank beneficiaryBank) {
		return AlizzTransferDto.Receiver.builder().notesToReceiver(alizzTransferRequestDto.getNotesToReceiver())
				.accountName(receiverAccDetails.getAdesc()).accountNumber(receiverAccDetails.getAcc())
				.bankCode(Objects.nonNull(beneficiaryBank) ? beneficiaryBank.getBankCode() : "")
				.bankName(Objects.nonNull(beneficiaryBank) ? beneficiaryBank.getBankName() : "")
				.branchCode(alizzTransferRequestDto.getFromAccountNumber().substring(0, 3)).iBanAccountNumber("")
				.bankAddress1("Muscat").bankAddress2("Muscat").bankAddress3("Muscat").bankAddress4("Muscat")
				.beneAddress1("Muscat").beneAddress2("Muscat").beneAddress3("Muscat").beneAddress4("Muscat")
				.bankCountry("Oman").build();
	}

	private AlizzTransferDto.Sender getSenderDetails(AlizzTransferRequestDto alizzTransferRequestDto,
			AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount senderAccDetails,
			BeneficiaryBank beneficiaryBank) {

		String senderCifNo = alizzTransferRequestDto.getFromAccountNumber().substring(3, 10);
		AlizzTransferDto.Sender sender = AlizzTransferDto.Sender.builder().accountNumber(senderAccDetails.getAcc())
				.accountCurrency(senderAccDetails.getCcy())
				.bankCode(Objects.nonNull(beneficiaryBank) ? beneficiaryBank.getBankCode() : "")
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
	}

	private AlizzTransferDto.Transaction getTransactionDetails(AlizzTransferRequestDto alizzTransferRequestDto,
			String txnRefId, AccountCurrency accountCurrency, TransferAccountFields transferAccountFields,
			String trnxDate) {
		return AlizzTransferDto.Transaction.builder().paymentDetails1(alizzTransferRequestDto.getNotesToReceiver())
				.paymentDetails2("").paymentDetails3("").paymentDetails4("").transactionReference(txnRefId)
				.transactionDate(trnxDate).transactionAmount(alizzTransferRequestDto.getTransactionAmount())
				.transactionPurpose(alizzTransferRequestDto.getTransactionPurpose())
				.transactionCurrency(accountCurrency.getAccountCurrency())
				.cbsModule(
						Objects.nonNull(transferAccountFields) && Objects.nonNull(transferAccountFields.getCbsModule())
								? transferAccountFields.getCbsModule()
								: "")
				.cbsNetwork(
						Objects.nonNull(transferAccountFields) && Objects.nonNull(transferAccountFields.getCbsNetwork())
								? transferAccountFields.getCbsNetwork()
								: "")
				.cbsProduct(
						Objects.nonNull(transferAccountFields) && Objects.nonNull(transferAccountFields.getCbsProduct())
								? transferAccountFields.getCbsProduct()
								: "")
				.chargeType(
						Objects.nonNull(transferAccountFields) && Objects.nonNull(transferAccountFields.getChargeType())
								? transferAccountFields.getChargeType()
								: "")
				.build();
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

	private AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount getIslamicAccount(String accountNumber) {
		String receiverCifNo = accountNumber.substring(3, 10);
		Optional<AccountDetails> receiverAccountInfo = getTokenAndApiResponse(receiverCifNo);
		if (receiverAccountInfo.isPresent()) {
			List<AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount> accountDetails = receiverAccountInfo
					.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts();
			return accountDetails.stream().filter(acc -> acc.getAcc().equals(accountNumber)).toList().get(0);
		} else {
			return null;
		}
	}

	private Optional<AccountDetails> getTokenAndApiResponse(String civilId) {
		ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
		if (Objects.nonNull(response.getBody())) {
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(response.getBody().getAccessToken());
			HttpEntity<String> entity = new HttpEntity<>(headers);

			String apiUrl = accountExternalAPI + civilId;
			ResponseEntity<AccountDetails> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, entity,
					AccountDetails.class);
			return Optional.ofNullable(responseEntity.getBody());
		}
		return Optional.empty();
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

	@Override
	public Double calculateFee(String transferType, String uniqueKey) {
		try {
			TransactionCode transactionCode = transactionCodeRepo
					.findByTransferType(TransferType.valueOf(transferType));
			ResponseEntity<AccessTokenResponse> tokenResponse = commonUtils.getToken();
			logger.info("tokenResponse {}", tokenResponse);
			if (tokenResponse.getStatusCode().is2xxSuccessful()) {

				Optional<Profile> profileOptional = profileRepository.findByUniqueKeyCivilId(uniqueKey);
				Profile profile = profileOptional.orElse(null);

				ModuleData data = ModuleData.builder().moduleCode(transactionCode.getCbsModule())
						.productCode(transactionCode.getCbsProduct()).customerNumber(Objects.nonNull(profile) ? profile.getNId() : "").accountNumber("")
						.fromDate("").toDate("").chequeLeaves("").transactionCurrency("").transactionAmount("")
						.tenure(0).eventCode("INIT").build();

				HttpHeaders headers = new HttpHeaders();
				String accessToken = Objects.requireNonNull(tokenResponse.getBody().getAccessToken());
				headers.setBearerAuth(accessToken);
				String jsonRequestBody = objectMapper.writeValueAsString(data);
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);

				ResponseEntity<ChargeApiResponse> chargeResponseEntity = restTemplate.exchange(chargeApi,
						HttpMethod.POST, requestEntity, ChargeApiResponse.class);
				logger.info("chargeResponse {}", chargeResponseEntity);
				if (chargeResponseEntity.getStatusCode().is2xxSuccessful()) {
					ChargeApiResponse chargeResponse = chargeResponseEntity.getBody();
					if (chargeResponse != null && chargeResponse.isSuccess()) {
						ChargeApiResponse.ChargeSheetEntry chargeEntry = chargeResponse.getChargeSheet().get(0);
						return Double.parseDouble(String.valueOf(chargeEntry.getChargeAmount()));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in calculateFee method : {}", e.getMessage());
		}

		return 0.0;
	}

}
