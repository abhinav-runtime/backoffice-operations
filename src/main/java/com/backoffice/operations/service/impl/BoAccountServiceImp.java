package com.backoffice.operations.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.entity.AccountTransactionsEntity;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.AccountDetails;
import com.backoffice.operations.payloads.AccountTransactionResponseDTO;
import com.backoffice.operations.payloads.ExternalApiTransactionResponseDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.BoAccountService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BoAccountServiceImp implements BoAccountService {
	private final Logger logger = LoggerFactory.getLogger(BoAccountServiceImp.class);
	@Value("${external.api.accounts}")
	private String externalAccountApiUrl;
	@Value("${external.api.accounts.transaction}")
	private String externalTransactionApiUrl;
	@Autowired
	@Qualifier("jwtAuth")
	private RestTemplate jwtAuthRestTemplate;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CommonUtils commonUtils;

	@Override
	public GenericResponseDTO<Object> getAccountDetails(String custNo) {
		logger.info("Fetching account details for customer number: {}", custNo);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();

		String accessToken = null;
		try {
			ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
			logger.info("response: {}", response.getBody());
			accessToken = Objects.requireNonNull(response.getBody().getAccessToken());
			logger.info("accessToken: {}", accessToken);

			String apiUrl = externalAccountApiUrl + custNo;
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(headers);
			ResponseEntity<String> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity,
					String.class);
//			ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity,
//					String.class);

			String jsonResponse = responseEntity.getBody();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode;

			jsonNode = mapper.readTree(jsonResponse);
			JsonNode name = jsonNode.at("/response/payload/custSummaryDetails/islamicAccounts");
			responseDTO.setData(name);
			responseDTO.setMessage("Account details fetched successfully");
			responseDTO.setStatus("Success");
			return responseDTO;
		} catch (Exception e) {
			logger.error("Error occurred while fetching account details: {}", e.getMessage());
			responseDTO.setData(new HashMap<>());
			responseDTO.setMessage("Something went wrong while fetching details");
			responseDTO.setStatus("Failure");
			return responseDTO;
		}
	}

	@Override
	public GenericResponseDTO<Object> getTransactionDetails(String accountNumber, String fromDate, String toDate) {
        ResponseEntity<AccessTokenResponse>  response = commonUtils.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(response.getBody().getAccessToken());
        StringBuilder requestBody = new StringBuilder();
        if (Objects.isNull(fromDate) && Objects.isNull(toDate)) {
            requestBody.append("{\n" + "\"accountNumber\": \"").append(accountNumber).append("\"\n" + " }");
        } else if (Objects.isNull(fromDate)) {
            requestBody.append("{\n" + "\"accountNumber\": \"").append(accountNumber)
                    .append("\",\n" + "\"toDate\":\"").append(toDate).append("\"\n" + " }");
        } else if (Objects.isNull(toDate)) {
            requestBody.append("{\n" + "\"accountNumber\": \"").append(accountNumber)
                    .append("\",\n" + "\"fromDate\":\"").append(fromDate).append("\"\n" + " }");
        }else {
            requestBody.append("{\n" + "\"accountNumber\": \"").append(accountNumber)
                    .append("\",\n" + "\"fromDate\":\"").append(fromDate)
                    .append("\",\n" + "\"toDate\":\"").append(toDate).append("\"\n" + " }");
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<ExternalApiTransactionResponseDTO> accountsTransactionResponseEntity = jwtAuthRestTemplate.exchange(externalTransactionApiUrl,
                HttpMethod.POST, requestEntity, ExternalApiTransactionResponseDTO.class);
        List<AccountTransactionsEntity> accountTransactionsEntityList = new ArrayList<>();
        if (Objects.nonNull(accountsTransactionResponseEntity.getBody()) && accountsTransactionResponseEntity.getBody().isSuccess()
                && Objects.nonNull(accountsTransactionResponseEntity.getBody().getResponse())
                && !accountsTransactionResponseEntity.getBody().getResponse().getTransactions().isEmpty()) {
            List<ExternalApiTransactionResponseDTO.Response.Transaction> transactionList = accountsTransactionResponseEntity.getBody().getResponse().getTransactions();
            AccountTransactionResponseDTO accountTransactionResponseDTOS = new AccountTransactionResponseDTO();
            List<AccountTransactionResponseDTO.AccountTransaction> accountTransactionsEntities = new ArrayList<>();

            String actualFirstFourDigits = accountNumber.substring(0, 4);
            String actualLastFourDigits = accountNumber.substring(accountNumber.length() - 4);
            String creditCardNumber = actualFirstFourDigits + "********" + actualLastFourDigits;

            String receiverCifNo = accountNumber.substring(3, 10);
            Optional<AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount> islamicAccountOptional = getTokenAndApiResponse(receiverCifNo)
                    .map(AccountDetails::getResponse)
                    .map(AccountDetails.Response::getPayload)
                    .map(AccountDetails.Response.Payload::getCustSummaryDetails)
                    .map(AccountDetails.Response.Payload.CustSummaryDetails::getIslamicAccounts)
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .filter(acc -> acc.getAcc().equals(accountNumber))
                    .findFirst();

            Optional<AccountDetails.Response.Payload.CustSummaryDetails.Istddetails> istddetailsOptional = islamicAccountOptional
                    .filter(islamicAccount -> islamicAccount != null)
                    .map(unused -> getTokenAndApiResponse(receiverCifNo)
                            .map(AccountDetails::getResponse)
                            .map(AccountDetails.Response::getPayload)
                            .map(AccountDetails.Response.Payload::getCustSummaryDetails)
                            .map(AccountDetails.Response.Payload.CustSummaryDetails::getIstddetails)
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .filter(acc -> acc.getCustacno().equals(accountNumber))
                            .findFirst()
                            .orElse(null) // Extract the value from the Optional
                    );

            AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount islamicAccount = islamicAccountOptional.orElse(null);
            AccountDetails.Response.Payload.CustSummaryDetails.Istddetails istddetailsAccount = istddetailsOptional.orElse(null);

            transactionList.forEach(txn -> {
                String indicator = txn.getIndicator();
                String desc = txn.getTransactionDesc();
                double amount = txn.getTxnAmount();
                String referenceNo = txn.getReferenceNumber();
                LocalDate localDate = LocalDate.parse(txn.getTransactionDate());
                AccountTransactionsEntity accountTransactionsEntity = AccountTransactionsEntity.builder()
                        .accountNumber(accountNumber).transactionAmount(txn.getTxnAmount()).transactionDate(LocalDate.parse(txn.getTransactionDate()))
                        .transactionDescription(txn.getTransactionDesc()).indicator(txn.getIndicator())
                        .referenceNumber(referenceNo).build();

                AccountTransactionResponseDTO.AccountTransaction transactionResponseDTO = AccountTransactionResponseDTO.AccountTransaction.builder()
                        .indicator(indicator).transactionAmount(amount).transactionDescription(desc).transactionDate(localDate)
                        .transactionFrom(creditCardNumber).availableBalance(Objects.nonNull(islamicAccount) ? islamicAccount.getAcyavlbal() : Objects.nonNull(istddetailsAccount) ? istddetailsAccount.getTdamt() : 0.0)
                        .referenceNumber(referenceNo).transactionTo("").build();
                accountTransactionsEntities.add(transactionResponseDTO);
                accountTransactionsEntityList.add(accountTransactionsEntity);
            });
            Collections.reverse(accountTransactionsEntities);
            ArrayList<AccountTransactionResponseDTO.AccountTransaction> reversedList = new ArrayList<>(accountTransactionsEntities);
            accountTransactionsEntities.clear();
            accountTransactionsEntities.addAll(reversedList);
            accountTransactionResponseDTOS.setAccountTransactions(accountTransactionsEntities);
            GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
            Map<String, Object> data = new HashMap<>();
            data.put("accountTransactions",accountTransactionResponseDTOS);
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(data);
            return responseDTO;
        }else {
            GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
            AccountTransactionResponseDTO accountTransactionResponseDTOS = AccountTransactionResponseDTO.builder()
                    .accountTransactions(List.of(AccountTransactionResponseDTO.AccountTransaction.builder().transactionAmount(0.0)
                                    .indicator("").transactionDate(LocalDate.now()).transactionDescription("")
                            .build())).build();
            Map<String, Object> data = new HashMap<>();
            data.put("accountTransactions",accountTransactionResponseDTOS);
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(data);
            return responseDTO;
        }
	}
	
	private Optional<AccountDetails> getTokenAndApiResponse(String civilId) {
        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
        if (Objects.nonNull(response.getBody())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String apiUrl = externalAccountApiUrl + civilId;
            ResponseEntity<AccountDetails> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, entity, AccountDetails.class);
            return Optional.ofNullable(responseEntity.getBody());
        }
        return Optional.empty();
    }

}
