package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.*;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.*;
import com.backoffice.operations.service.DashboardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Value("${external.api.accounts}")

    private String accountExternalAPI;

    @Value("${external.api.customer.fetchDue}")
    private String creditCardFetchDue;

    @Value("${external.api.url}")
    private String externalApiUrl;

    @Value("${external.api.accounts.transaction}")
    private String externalAccountsTransactionApiUrl;

    @Value("${external.api.credit.card.transaction}")
    private String externalCardTransactionApiUrl;


    private final RestTemplate restTemplate;

    private final CivilIdRepository civilIdRepository;

    private final DashboardRepository dashboardRepository;

    private final DashboardInfoRepository dashboardInfoRepository;


    private final AccountTransactionsEntityRepository accountTransactionsEntityRepository;

    private final CardTransactionsEntityRepository cardTransactionsEntityRepository;

    public DashboardServiceImpl(RestTemplate restTemplate, CivilIdRepository civilIdRepository, DashboardRepository dashboardRepository, DashboardInfoRepository dashboardInfoRepository, AccountTransactionsEntityRepository accountTransactionsEntityRepository, CardTransactionsEntityRepository cardTransactionsEntityRepository) {

        this.restTemplate = restTemplate;
        this.civilIdRepository = civilIdRepository;
        this.dashboardRepository = dashboardRepository;
        this.dashboardInfoRepository = dashboardInfoRepository;

        this.accountTransactionsEntityRepository = accountTransactionsEntityRepository;
        this.cardTransactionsEntityRepository = cardTransactionsEntityRepository;

    }

    @Override
    public GenericResponseDTO<Object> getDashboardDetails(String uniqueKey) {

        Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(uniqueKey);

        if(civilIdEntity.isPresent()) {

            String apiUrl = accountExternalAPI + civilIdEntity.get().getCivilId();
            ResponseEntity<AccountDetails> responseEntity = restTemplate.getForEntity(apiUrl,
                    AccountDetails.class);

            AccountDetails apiResponse = responseEntity.getBody();

            if (apiResponse != null && apiResponse.isSuccess()) {
                List<AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount> islamicAccountList = apiResponse.getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts();
                AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount islamicAccount = islamicAccountList.get(0);
                DashboardEntity dashboardEntity = DashboardEntity.builder().id(uniqueKey).accountNumber(islamicAccount.getAcc())
                        .availableBalance(islamicAccount.getAcyavlbal()).currency(islamicAccount.getCcy()).build();
                DashboardEntity dashboardEntityObject = dashboardRepository.save(dashboardEntity);
                GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                Map<String, Object> data = new HashMap<>();
                AccountsDetailsResponseDTO accountsDetailsResponseDTO = new AccountsDetailsResponseDTO();
                accountsDetailsResponseDTO.setAccountNumber(dashboardEntityObject.getAccountNumber());
                accountsDetailsResponseDTO.setAccountBalance(dashboardEntityObject.getAvailableBalance());
                accountsDetailsResponseDTO.setCurrency(dashboardEntityObject.getCurrency());
                data.put("uniqueKey",dashboardEntityObject.getId());
                data.put("accountDetails",List.of(accountsDetailsResponseDTO));
                responseDTO.setStatus("Success");
                responseDTO.setMessage("Success");
                responseDTO.setData(data);
                return responseDTO;
            }
        }
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();
        responseDTO.setStatus("Failure");
        responseDTO.setMessage("No Entry Found");
        data.put("uniqueKey",uniqueKey);
        responseDTO.setData(data);
        return  responseDTO;
    }

    @Override
    public GenericResponseDTO<Object> getDashboardInfo(String uniqueKey) {

        Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(uniqueKey);
        if (civilIdEntity.isPresent()) {

            String cardListUrl = externalApiUrl + "/getCardList";
            HttpHeaders headers = new HttpHeaders();
            headers.add("TENANT", "ALIZZ_UAT");
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestBody = "{ \"customerId\": \"" + civilIdEntity.get().getCivilId() + "\" }";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<ExternalApiResponseDTO> cardListresponseEntity = restTemplate.exchange(cardListUrl,
                    HttpMethod.POST, requestEntity, ExternalApiResponseDTO.class);

            if (Objects.nonNull(cardListresponseEntity.getBody()) && Objects.nonNull(cardListresponseEntity.getBody().getResult()) && !cardListresponseEntity.getBody().getResult().getCardList().isEmpty()) {
                String customerName = cardListresponseEntity.getBody().getResult().getName();
                List<ExternalApiResponseDTO.Result.Card> cardList = cardListresponseEntity.getBody().getResult().getCardList();
                List<CreditCardDetailsResponseDTO> creditCardDetailsResponseList = new ArrayList<>();


                String entityRequestBody = "{ \"entityId\": \"" + civilIdEntity.get().getCivilId() + "\" }";
                HttpEntity<String> fetchDueRequestEntity = new HttpEntity<>(entityRequestBody, headers);
                ResponseEntity<ExternalApiFetchDueResponseDTO> fetchDueResponseEntity = restTemplate.exchange(creditCardFetchDue,
                        HttpMethod.POST, fetchDueRequestEntity, ExternalApiFetchDueResponseDTO.class);

                ResponseEntity<ExternalApiFetchBalanceResponseDTO> fetchBalanceResponseEntity = restTemplate.exchange(externalApiUrl + "/fetchLimit",
                        HttpMethod.POST, fetchDueRequestEntity, ExternalApiFetchBalanceResponseDTO.class);

                if (Objects.nonNull(fetchDueResponseEntity.getBody()) && Objects.nonNull(fetchDueResponseEntity.getBody().getResult())) {
                    if (Objects.nonNull(fetchBalanceResponseEntity.getBody()) && Objects.nonNull(fetchBalanceResponseEntity.getBody().getResult())) {

                        ExternalApiFetchDueResponseDTO.Result fetchDueResult = fetchDueResponseEntity.getBody().getResult();
                        cardList.forEach(card -> {
                            String actualFirstFourDigits = card.getCardNo().substring(0, 4);
                            String actualLastFourDigits = card.getCardNo().substring(card.getCardNo().length() - 4);
                            String creditCardNumber = actualFirstFourDigits + "********" + actualLastFourDigits;
                            Double outStandingAmount = Double.parseDouble(fetchDueResult.getTotalOutStandingAmount());
                            Double availableAmount = Double.parseDouble(fetchBalanceResponseEntity.getBody().getResult().getLimitAvailable());
                            CreditCardDetailsResponseDTO creditCardDetailsResponseDTO = new CreditCardDetailsResponseDTO();
                            creditCardDetailsResponseDTO.setAvailableBalance(availableAmount);
                            creditCardDetailsResponseDTO.setOutstandingBalance(outStandingAmount);
                            creditCardDetailsResponseDTO.setCustomerName(customerName);
                            creditCardDetailsResponseDTO.setCreditCardNumber(creditCardNumber);
                            creditCardDetailsResponseList.add(creditCardDetailsResponseDTO);

                            DashboardInfoEntity dashboardInfoEntity = DashboardInfoEntity.builder().creditCardNumber(creditCardNumber)
                                    .customerName(customerName).outstandingBal(outStandingAmount).availableBal(availableAmount).build();
                            dashboardInfoRepository.save(dashboardInfoEntity);
                        });
                    } else {
                        return getErrorResponseObject(uniqueKey);
                    }
                } else {
                    return getErrorResponseObject(uniqueKey);
                }

                GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                Map<String, Object> data = new HashMap<>();
                data.put("creditCardDetails",creditCardDetailsResponseList);
                data.put("uniqueKey",uniqueKey);
                responseDTO.setStatus("Success");
                responseDTO.setMessage("Success");
                responseDTO.setData(data);
                return responseDTO;
            } else {
                return getErrorResponseObject(uniqueKey);
            }
        }
        return getErrorResponseObject(uniqueKey);
    }

    @Override
    public GenericResponseDTO<Object> getAccountTransactions(String accountNumber, String fromDate, String toDate, String uniqueKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        ResponseEntity<ExternalApiTransactionResponseDTO> accountsTransactionResponseEntity = restTemplate.exchange(externalAccountsTransactionApiUrl,
                HttpMethod.POST, requestEntity, ExternalApiTransactionResponseDTO.class);
        List<AccountTransactionsEntity> accountTransactionsEntityList = new ArrayList<>();
        if (Objects.nonNull(accountsTransactionResponseEntity.getBody()) && accountsTransactionResponseEntity.getBody().isSuccess()
                && Objects.nonNull(accountsTransactionResponseEntity.getBody().getResponse())
                && !accountsTransactionResponseEntity.getBody().getResponse().getTransactions().isEmpty()) {
            List<ExternalApiTransactionResponseDTO.Response.Transaction> transactionList = accountsTransactionResponseEntity.getBody().getResponse().getTransactions();
            AccountTransactionResponseDTO accountTransactionResponseDTOS = new AccountTransactionResponseDTO();
            List<AccountTransactionResponseDTO.AccountTransaction> accountTransactionsEntities = new ArrayList<>();
            transactionList.forEach(txn -> {
                String indicator = txn.getIndicator();
                String desc = txn.getTransactionDesc();
                double amount = txn.getTxnAmount();
                String referenceNo = txn.getReferenceNumber();
                LocalDate localDate = LocalDate.parse(txn.getTransactionDate());
                AccountTransactionsEntity accountTransactionsEntity = AccountTransactionsEntity.builder().id(uniqueKey)
                        .accountNumber(accountNumber).transactionAmount(txn.getTxnAmount()).transactionDate(LocalDate.parse(txn.getTransactionDate()))
                        .transactionDescription(txn.getTransactionDesc()).indicator(txn.getIndicator())
                        .referenceNumber(referenceNo).build();

                AccountTransactionResponseDTO.AccountTransaction transactionResponseDTO = AccountTransactionResponseDTO.AccountTransaction.builder()
                        .indicator(indicator).transactionAmount(amount).transactionDescription(desc).transactionDate(localDate)
                        .referenceNumber(referenceNo).build();
                accountTransactionsEntities.add(transactionResponseDTO);
                accountTransactionsEntityList.add(accountTransactionsEntity);
            });

            accountTransactionsEntityRepository.saveAll(accountTransactionsEntityList);
            accountTransactionResponseDTOS.setAccountTransactions(accountTransactionsEntities);
            GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
            Map<String, Object> data = new HashMap<>();
            data.put("accountTransactions",accountTransactionResponseDTOS);
            data.put("uniqueKey",uniqueKey);
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
            data.put("uniqueKey",uniqueKey);
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(data);
            return responseDTO;
        }
    }

    @Override
    public GenericResponseDTO<Object> getCreditCardTransactions(String fromDate, String toDate, String uniqueKey) {

        Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(uniqueKey);

        if(civilIdEntity.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("TENANT", "ALIZZ_UAT");
            String apiUrl = externalCardTransactionApiUrl + "?pageNo=0&pageSize=999&txnCategory=spend";
            String requestBody = "{ \"entityId\": \"" + civilIdEntity.get().getCivilId() + "\" }";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<ExternalApiCardTransactionResponseDTO> cardTransactionResponseEntity = restTemplate.exchange(apiUrl,
                    HttpMethod.POST, requestEntity, ExternalApiCardTransactionResponseDTO.class);
            if (Objects.nonNull(cardTransactionResponseEntity.getBody()) && Objects.nonNull(cardTransactionResponseEntity.getBody().getResult())
                    && !cardTransactionResponseEntity.getBody().getResult().isEmpty()) {
                List<ExternalApiCardTransactionResponseDTO.Transaction> transactionList = cardTransactionResponseEntity.getBody().getResult();

                List<CardTransactionResponseDTO.CardTransaction> cardTransactionsList = new ArrayList<>();
                List<CardTransactionsEntity> cardTransactionsEntities = new ArrayList<>();
                transactionList.forEach(txn -> {
                    Instant instant = Instant.ofEpochMilli(txn.getTransactionDate());
                    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
                    LocalDate dt = zonedDateTime.toLocalDate();
                    double amt = Double.parseDouble(txn.getAmount());
                    String desc = txn.getMerchantName();
                    String indicator = txn.getCrDr();
                    String refNo = txn.getTxId();
                    CardTransactionsEntity cardTransactionsEntity = CardTransactionsEntity.builder()
                            .transactionDate(dt).transactionAmount(amt).transactionDescription(desc)
                            .referenceNumber(refNo).indicator(indicator).build();
                    cardTransactionsEntities.add(cardTransactionsEntity);
                    CardTransactionResponseDTO.CardTransaction cardTransaction = CardTransactionResponseDTO.CardTransaction.builder()
                            .transactionDate(dt).transactionAmount(amt).transactionDescription(desc).indicator(indicator)
                            .referenceNumber(refNo).build();
                    cardTransactionsList.add(cardTransaction);
                });
                cardTransactionsEntityRepository.saveAll(cardTransactionsEntities);
                CardTransactionResponseDTO cardTransactionResponseDTO = CardTransactionResponseDTO.builder()
                        .cardTransactions(cardTransactionsList).build();
                GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                Map<String, Object> data = new HashMap<>();
                data.put("cardTransactions",cardTransactionResponseDTO);
                data.put("uniqueKey",uniqueKey);
                responseDTO.setStatus("Success");
                responseDTO.setMessage("Success");
                responseDTO.setData(data);
                return responseDTO;
            }else {
                GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                CardTransactionResponseDTO cardTransactionResponseDTO = CardTransactionResponseDTO.builder()
                        .cardTransactions(List.of(CardTransactionResponseDTO.CardTransaction.builder()
                                .transactionDate(LocalDate.now()).transactionAmount(0.0).transactionDescription("").indicator("")
                                .referenceNumber("").build())).build();
                Map<String, Object> data = new HashMap<>();
                data.put("cardTransactions",cardTransactionResponseDTO);
                data.put("uniqueKey",uniqueKey);
                responseDTO.setStatus("Success");
                responseDTO.setMessage("Success");
                responseDTO.setData(data);
                return responseDTO;
            }
        }else {
            GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
            CardTransactionResponseDTO cardTransactionResponseDTO = CardTransactionResponseDTO.builder()
                    .cardTransactions(List.of(CardTransactionResponseDTO.CardTransaction.builder()
                            .transactionDate(LocalDate.now()).transactionAmount(0.0).transactionDescription("").indicator("")
                            .referenceNumber("").build())).build();
            Map<String, Object> data = new HashMap<>();
            data.put("cardTransactions",cardTransactionResponseDTO);
            data.put("uniqueKey",uniqueKey);
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(data);
            return responseDTO;
        }
    }

    private static GenericResponseDTO<Object> getErrorResponseObject(String uniqueKey) {
        CreditCardDetailsResponseDTO creditCardDetailsResponseDTO = new CreditCardDetailsResponseDTO();
        creditCardDetailsResponseDTO.setAvailableBalance(0.0);
        creditCardDetailsResponseDTO.setOutstandingBalance(0.0);
        creditCardDetailsResponseDTO.setCustomerName("");
        creditCardDetailsResponseDTO.setCreditCardNumber("");
        List<CreditCardDetailsResponseDTO> creditCardDetailsResponseList = new ArrayList<>();
        creditCardDetailsResponseList.add(creditCardDetailsResponseDTO);
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();
        data.put("creditCardDetails",creditCardDetailsResponseList);
        data.put("uniqueKey", uniqueKey);
        responseDTO.setStatus("Success");
        responseDTO.setMessage("Success");
        responseDTO.setData(data);
        return responseDTO;
    }


}
