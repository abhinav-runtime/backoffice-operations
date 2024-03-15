package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.*;
import com.backoffice.operations.enums.JointTypeEnum;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.*;
import com.backoffice.operations.service.DashboardService;
import com.backoffice.operations.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    @Autowired
    @Qualifier("jwtAuth")
    private RestTemplate jwtAuthRestTemplate;

    @Autowired
    private CommonUtils commonUtils;

    private final RestTemplate restTemplate;

    private final CivilIdRepository civilIdRepository;

    private final DashboardRepository dashboardRepository;

    private final DashboardInfoRepository dashboardInfoRepository;

    private final AccountTransactionsEntityRepository accountTransactionsEntityRepository;

    private final CardTransactionsEntityRepository cardTransactionsEntityRepository;

    private final AccountTypeRepository accountTypeRepository;

    private final String account = "account";

    private static final String creditCard = "card";

    private final ProfileRepository profileRepository;

    public DashboardServiceImpl(RestTemplate restTemplate, CivilIdRepository civilIdRepository, DashboardRepository dashboardRepository, DashboardInfoRepository dashboardInfoRepository, AccountTransactionsEntityRepository accountTransactionsEntityRepository, CardTransactionsEntityRepository cardTransactionsEntityRepository, AccountTypeRepository accountTypeRepository, ProfileRepository profileRepository) {
        this.restTemplate = restTemplate;
        this.civilIdRepository = civilIdRepository;
        this.dashboardRepository = dashboardRepository;
        this.dashboardInfoRepository = dashboardInfoRepository;
        this.accountTransactionsEntityRepository = accountTransactionsEntityRepository;
        this.cardTransactionsEntityRepository = cardTransactionsEntityRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public GenericResponseDTO<Object> getDashboardDetails(String uniqueKey) {
        return civilIdRepository.findById(uniqueKey)
                .flatMap(civilIdEntity -> getTokenAndApiResponse(civilIdEntity.getCivilId()))
                .map(apiResponse -> {

                    Optional<Profile> profileOptional = profileRepository.findByUniqueKeyCivilId(uniqueKey);
                    Profile profile = profileOptional.orElse(null);
                    String custFullName = Objects.nonNull(profile) ? profile.getFullName() : "";

                    List<AccountsDetailsResponseDTO> accountsDetailsResponseDTOList = apiResponse.getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().stream()
                            .map(islamicAccount -> {
                                AccountType accountType = getAccountDescription(islamicAccount.getAccls());
                                String accountCodeDesc = Objects.nonNull(accountType) && Objects.nonNull(accountType.getDescription()) ? accountType.getDescription() : "";
                                String accNickName = Objects.nonNull(accountType) && Objects.nonNull(accountType.getAccountNickName()) ? accountType.getAccountNickName() : "";
                                String requestDebitCard = Objects.nonNull(accountType) && Objects.nonNull(accountType.getRequestDebitCard()) ? accountType.getRequestDebitCard() : "N";
                                String requestsChequeBook = Objects.nonNull(accountType) && Objects.nonNull(accountType.getRequestsChequeBook()) ? accountType.getRequestsChequeBook() : "N";
                                String billPayments = Objects.nonNull(accountType) && Objects.nonNull(accountType.getBillPayments()) ? accountType.getBillPayments() : "N";
                                String transfers = Objects.nonNull(accountType) && Objects.nonNull(accountType.getTransfers()) ? accountType.getTransfers() : "N";
                                String editAccountInfo = Objects.nonNull(accountType) && Objects.nonNull(accountType.getEditAccountInfo()) ? accountType.getEditAccountInfo() : "N";
                                String visibility = Objects.nonNull(accountType) && Objects.nonNull(accountType.getVisibility()) ? accountType.getVisibility() : "N";

                                DashboardEntity dashboard = dashboardRepository.findByAccountNumberAndUniqueKey(islamicAccount.getAcc(),uniqueKey);

                                DashboardEntity dashboardEntity = DashboardEntity.builder()
                                        .uniqueKey(uniqueKey)
                                        .accountNumber(islamicAccount.getAcc())
                                        .availableBalance(islamicAccount.getAcyavlbal())
                                        .currency(islamicAccount.getCcy())
                                        .accountCodeDesc(accountCodeDesc)
                                        .accountType(islamicAccount.getAcctype())
                                        .accountNickName(accNickName)
                                        .isAccountVisible(!Objects.nonNull(dashboard) || dashboard.isAccountVisible()).currentAccountBalance(islamicAccount.getCurrbal())
                                        .branchName(islamicAccount.getBrn()).blockedAmount(islamicAccount.getAcyblkamt())
                                        .customerName(custFullName).isAlertOnLowBal(!Objects.nonNull(dashboard) || dashboard.isAlertOnLowBal())
                                        .isAlertOnTrnx(!Objects.nonNull(dashboard) || dashboard.isAlertOnTrnx())
                                        .jointType(JointTypeEnum.getValueByCode(islamicAccount.getAcctype()))
                                        .lowBalLimit(Objects.nonNull(dashboard) ? dashboard.getLowBalLimit() : 0)
                                        .openDate(islamicAccount.getStatsince()).shariaContract("")
                                        .customerNickName(Objects.nonNull(dashboard) ? dashboard.getCustomerNickName() : "")
                                        .requestDebitCard(requestDebitCard).requestsChequeBook(requestsChequeBook).billPayments(billPayments)
                                        .transfers(transfers).editAccountInfo(editAccountInfo).visibility(visibility)
                                        .build();
                                if(Objects.nonNull(dashboard)){
                                    dashboardEntity.setId(dashboard.getId());
                                }
                                dashboardEntity = dashboardRepository.save(dashboardEntity);
                                return AccountsDetailsResponseDTO.builder()
                                        .accountBalance(Objects.nonNull(dashboardEntity.getAvailableBalance()) ? dashboardEntity.getAvailableBalance() : 0.0)
                                        .accountNumber(Objects.nonNull(dashboardEntity.getAccountNumber()) ? dashboardEntity.getAccountNumber() : "")
                                        .accountType(Objects.nonNull(dashboardEntity.getAccountType()) ? dashboardEntity.getAccountType() : "")
                                        .accountCodeDesc(Objects.nonNull(dashboardEntity.getAccountCodeDesc()) ? dashboardEntity.getAccountCodeDesc(): "")
                                        .currency(Objects.nonNull(dashboardEntity.getCurrency()) ? dashboardEntity.getCurrency() : "")
                                        .isAccountVisible(dashboardEntity.isAccountVisible()).currentAccountBalance(dashboardEntity.getCurrentAccountBalance())
                                        .branchName(dashboardEntity.getBranchName()).blockedAmount(dashboardEntity.getBlockedAmount())
                                        .customerName(custFullName).isAlertOnLowBal(dashboardEntity.isAlertOnLowBal())
                                        .isAlertOnTrnx(dashboardEntity.isAlertOnTrnx()).jointType(dashboardEntity.getJointType())
                                        .lowBalLimit(dashboardEntity.getLowBalLimit()).openDate(dashboardEntity.getOpenDate()).shariaContract("")
                                        .customerNickName(dashboardEntity.getCustomerNickName())
                                        .type(account)
                                        .accountNickName(Objects.nonNull(dashboardEntity.getAccountNickName()) ? dashboardEntity.getAccountNickName() : "")
                                        .requestDebitCard(requestDebitCard).requestsChequeBook(requestsChequeBook).billPayments(billPayments)
                                        .transfers(transfers).editAccountInfo(editAccountInfo).visibility(visibility).build();
                            })
                            .collect(Collectors.toList());

                    List<AccountsDetailsResponseDTO> accountsDetailsResponse = apiResponse.getResponse().getPayload().getCustSummaryDetails().getIstddetails().stream()
                            .map(istdDetails -> {
                                AccountType accountType = getAccountDescription(istdDetails.getAccclass());
                                String accountCodeDesc = Objects.nonNull(accountType) && Objects.nonNull(accountType.getDescription()) ? accountType.getDescription() : "";
                                String accNickName = Objects.nonNull(accountType) && Objects.nonNull(accountType.getAccountNickName()) ? accountType.getAccountNickName() : "";
                                String requestDebitCard = Objects.nonNull(accountType) && Objects.nonNull(accountType.getRequestDebitCard()) ? accountType.getRequestDebitCard() : "N";
                                String requestsChequeBook = Objects.nonNull(accountType) && Objects.nonNull(accountType.getRequestsChequeBook()) ? accountType.getRequestsChequeBook() : "N";
                                String billPayments = Objects.nonNull(accountType) && Objects.nonNull(accountType.getBillPayments()) ? accountType.getBillPayments() : "N";
                                String transfers = Objects.nonNull(accountType) && Objects.nonNull(accountType.getTransfers()) ? accountType.getTransfers() : "N";
                                String editAccountInfo = Objects.nonNull(accountType) && Objects.nonNull(accountType.getEditAccountInfo()) ? accountType.getEditAccountInfo() : "N";
                                String visibility = Objects.nonNull(accountType) && Objects.nonNull(accountType.getVisibility()) ? accountType.getVisibility() : "N";

                                DashboardEntity dashboard = dashboardRepository.findByAccountNumberAndUniqueKey(istdDetails.getCustacno(),uniqueKey);

                                DashboardEntity dashboardEntity = DashboardEntity.builder()
                                        .uniqueKey(uniqueKey)
                                        .accountNumber(istdDetails.getCustacno())
                                        .availableBalance(istdDetails.getTdamt())
                                        .currency(istdDetails.getCcy())
                                        .accountCodeDesc(accountCodeDesc)
                                        .accountType(istdDetails.getAcctype())
                                        .accountNickName(accNickName)
                                        .isAccountVisible(!Objects.nonNull(dashboard) || dashboard.isAccountVisible()).currentAccountBalance(istdDetails.getTdamt())
                                        .branchName(istdDetails.getBranch()).blockedAmount(0)
                                        .customerName(custFullName).isAlertOnLowBal(!Objects.nonNull(dashboard) || dashboard.isAlertOnLowBal())
                                        .isAlertOnTrnx(!Objects.nonNull(dashboard) || dashboard.isAlertOnTrnx())
                                        .jointType(JointTypeEnum.getValueByCode(istdDetails.getAcctype()))
                                        .lowBalLimit(Objects.nonNull(dashboard) ? dashboard.getLowBalLimit() : 0)
                                        .openDate(istdDetails.getStatsince()).shariaContract("")
                                        .customerNickName(Objects.nonNull(dashboard) ? dashboard.getCustomerNickName() : "")
                                        .requestDebitCard(requestDebitCard).requestsChequeBook(requestsChequeBook).billPayments(billPayments)
                                        .transfers(transfers).editAccountInfo(editAccountInfo).visibility(visibility)
                                        .build();
                                if(Objects.nonNull(dashboard)){
                                    dashboardEntity.setId(dashboard.getId());
                                }
                                dashboardRepository.save(dashboardEntity);
                                return AccountsDetailsResponseDTO.builder()
                                        .accountBalance(Objects.nonNull(dashboardEntity.getAvailableBalance()) ? dashboardEntity.getAvailableBalance() : 0.0)
                                        .accountNumber(Objects.nonNull(dashboardEntity.getAccountNumber()) ? dashboardEntity.getAccountNumber() : "")
                                        .accountType(Objects.nonNull(dashboardEntity.getAccountType()) ? dashboardEntity.getAccountType() : "")
                                        .accountCodeDesc(Objects.nonNull(dashboardEntity.getAccountCodeDesc()) ? dashboardEntity.getAccountCodeDesc(): "")
                                        .currency(Objects.nonNull(dashboardEntity.getCurrency()) ? dashboardEntity.getCurrency() : "")
                                        .type(account)
                                        .isAccountVisible(dashboardEntity.isAccountVisible()).currentAccountBalance(dashboardEntity.getCurrentAccountBalance())
                                        .branchName(dashboardEntity.getBranchName()).blockedAmount(dashboardEntity.getBlockedAmount())
                                        .customerName(custFullName).isAlertOnLowBal(dashboardEntity.isAlertOnLowBal())
                                        .isAlertOnTrnx(dashboardEntity.isAlertOnTrnx()).jointType(dashboardEntity.getJointType())
                                        .lowBalLimit(dashboardEntity.getLowBalLimit()).openDate(dashboardEntity.getOpenDate()).shariaContract("")
                                        .customerNickName(dashboardEntity.getCustomerNickName())
                                        .type(account)
                                        .accountNickName(Objects.nonNull(dashboardEntity.getAccountNickName()) ? dashboardEntity.getAccountNickName() : "")
                                        .requestDebitCard(requestDebitCard).requestsChequeBook(requestsChequeBook).billPayments(billPayments)
                                        .transfers(transfers).editAccountInfo(editAccountInfo).visibility(visibility)
                                        .build();
                            })
                            .toList();

                    accountsDetailsResponseDTOList.addAll(accountsDetailsResponse);

                    GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                    Map<String, Object> data = new HashMap<>();
                    data.put("uniqueKey", uniqueKey);
                    data.put("accountDetails", accountsDetailsResponseDTOList);
                    responseDTO.setStatus("Success");
                    responseDTO.setMessage("Success");
                    responseDTO.setData(data);
                    return responseDTO;
                })
                .orElseGet(() -> createFailureResponse(uniqueKey));
    }

    private AccountType getAccountDescription(String accls) {
        return accountTypeRepository.findByCbsProductCode(accls);
    }

    private Optional<AccountDetails> getTokenAndApiResponse(String civilId) {
        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
        if (Objects.nonNull(response.getBody())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String apiUrl = accountExternalAPI + civilId;
            ResponseEntity<AccountDetails> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, entity, AccountDetails.class);
            return Optional.ofNullable(responseEntity.getBody());
        }
        return Optional.empty();
    }

    private GenericResponseDTO<Object> createFailureResponse(String uniqueKey) {
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();
        responseDTO.setStatus("Failure");
        responseDTO.setMessage("No Entry Found");
        data.put("uniqueKey", uniqueKey);
        responseDTO.setData(data);
        return responseDTO;
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
                            creditCardDetailsResponseDTO.setType(creditCard);
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
        ResponseEntity<ExternalApiTransactionResponseDTO> accountsTransactionResponseEntity = jwtAuthRestTemplate.exchange(externalAccountsTransactionApiUrl,
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
                AccountTransactionsEntity accountTransactionsEntity = AccountTransactionsEntity.builder().id(uniqueKey)
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

            accountTransactionsEntityRepository.saveAll(accountTransactionsEntityList);
            Collections.reverse(accountTransactionsEntities);
            ArrayList<AccountTransactionResponseDTO.AccountTransaction> reversedList = new ArrayList<>(accountTransactionsEntities);
            accountTransactionsEntities.clear();
            accountTransactionsEntities.addAll(reversedList);
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
            StringBuilder requestBody = new StringBuilder();
            if (Objects.isNull(fromDate) && Objects.isNull(toDate)) {
                requestBody.append("{\n" + "\"entityId\": \"").append(civilIdEntity.get().getCivilId()).append("\"\n" + " }");
            } else if (Objects.isNull(fromDate)) {
                requestBody.append("{\n" + "\"entityId\": \"").append(civilIdEntity.get().getCivilId())
                        .append("\",\n" + "\"toDate\":\"").append(toDate).append("\"\n" + " }");
            } else if (Objects.isNull(toDate)) {
                requestBody.append("{\n" + "\"entityId\": \"").append(civilIdEntity.get().getCivilId())
                        .append("\",\n" + "\"fromDate\":\"").append(fromDate).append("\"\n" + " }");
            }else {
                requestBody.append("{\n" + "\"entityId\": \"").append(civilIdEntity.get().getCivilId())
                        .append("\",\n" + "\"fromDate\":\"").append(fromDate)
                        .append("\",\n" + "\"toDate\":\"").append(toDate).append("\"\n" + " }");
            }

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
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

    @Override
    public GenericResponseDTO<Object> getUpComingBills(String uniqueKey) {
        return civilIdRepository.findById(uniqueKey)
                .flatMap(civilIdEntity -> getTokenAndApiResponse(civilIdEntity.getCivilId()))
                .map(apiResponse -> {
                    UpcomingBillsResponseDto upcomingBillsResponseDtos = new UpcomingBillsResponseDto();
                    AtomicReference<Double> totalAmt = new AtomicReference<>((double) 0);
                    List<UpcomingBillsResponseDto.Events> eventsList = apiResponse.getResponse().getPayload().getCustSummaryDetails().getUpcomingEvents().stream()
                            .map(upcomingEvents -> {
                                totalAmt.updateAndGet(currentTotal -> currentTotal + upcomingEvents.getAmount());
                                 return UpcomingBillsResponseDto.Events.builder().event(upcomingEvents.getEvent())
                                        .amount(upcomingEvents.getAmount()).dueOn(upcomingEvents.getEventdt())
                                        .currency(upcomingEvents.getCcy()).build();

                            })
                            .toList();

                    upcomingBillsResponseDtos.setEventsList(eventsList);
                    upcomingBillsResponseDtos.setTotalAmt(totalAmt.get());
                    GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                    Map<String, Object> data = new HashMap<>();
                    data.put("uniqueKey", uniqueKey);
                    data.put("upcomingEventsDetails", upcomingBillsResponseDtos);
                    responseDTO.setStatus("Success");
                    responseDTO.setMessage("Success");
                    responseDTO.setData(data);
                    return responseDTO;
                })
                .orElseGet(() -> createFailureResponse(uniqueKey));

    }

    @Override
    public GenericResponseDTO<Object> editInfo(EditInfoRequestDto editInfoRequestDto) {
        DashboardEntity dashboardEntity =dashboardRepository.findByAccountNumberAndUniqueKey(editInfoRequestDto.getAccountNumber(), editInfoRequestDto.getUniqueKey());
        if(Objects.nonNull(dashboardEntity)){
            dashboardEntity.setId(dashboardEntity.getId());
            dashboardEntity.setAccountVisible(editInfoRequestDto.getIsAccountVisible());
            dashboardEntity.setAlertOnLowBal(editInfoRequestDto.getIsAlertOnLowBal());
            dashboardEntity.setAlertOnTrnx(editInfoRequestDto.getIsAlertOnTrnx());
            dashboardEntity.setCustomerNickName(Objects.nonNull(editInfoRequestDto.getCustomerNickName()) ? editInfoRequestDto.getCustomerNickName() : "");
            dashboardEntity.setLowBalLimit(editInfoRequestDto.getLowBalLimit());
            dashboardRepository.save(dashboardEntity);
            GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
            Map<String, Object> data = new HashMap<>();
            data.put("accountNumber",editInfoRequestDto.getAccountNumber());
            data.put("uniqueKey", editInfoRequestDto.getUniqueKey());
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(data);
            return responseDTO;

        }
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();
        data.put("accountNumber",editInfoRequestDto.getAccountNumber());
        data.put("uniqueKey", editInfoRequestDto.getUniqueKey());
        data.put("message", "Account Not Found");
        responseDTO.setStatus("failure");
        responseDTO.setMessage("failure");
        responseDTO.setData(data);
        return responseDTO;
    }

    @Override
    public GenericResponseDTO<Object> getBlockedAmounts(String uniqueKey, String accountNumber) {

        return civilIdRepository.findById(uniqueKey)
                .flatMap(civilIdEntity -> getTokenAndApiResponse(civilIdEntity.getCivilId()))
                .map(apiResponse -> {
                    List<AccountDetails.Response.Payload.CustSummaryDetails.AmountBlocks> amountBlocks = apiResponse.getResponse().getPayload().getCustSummaryDetails().getAmountBlocks();
                    List<BlockedAmountResponseDto> blockedAmountList = amountBlocks.stream()
                            .filter(blkAmt -> blkAmt.getAccount().equals(accountNumber))
                            .map(amtBlock -> BlockedAmountResponseDto.builder()
                                    .amount(amtBlock.getAmount()).account(amtBlock.getAccount()).blktype(amtBlock.getBlktype())
                                    .branch(amtBlock.getBranch()).effdate(amtBlock.getEffdate())
                                    .amtblkno(amtBlock.getAmtblkno()).expdate(amtBlock.getExpdate())
                                    .build())
                            .toList();

                    GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                    Map<String, Object> data = new HashMap<>();
                    data.put("uniqueKey", uniqueKey);
                    data.put("blockedAmounts", blockedAmountList);
                    responseDTO.setStatus("Success");
                    responseDTO.setMessage("Success");
                    responseDTO.setData(data);
                    return responseDTO;

                })
                .orElseGet(() -> createFailureResponse(uniqueKey));
    }

    private static GenericResponseDTO<Object> getErrorResponseObject(String uniqueKey) {
        CreditCardDetailsResponseDTO creditCardDetailsResponseDTO = new CreditCardDetailsResponseDTO();
        creditCardDetailsResponseDTO.setAvailableBalance(0.0);
        creditCardDetailsResponseDTO.setOutstandingBalance(0.0);
        creditCardDetailsResponseDTO.setCustomerName("");
        creditCardDetailsResponseDTO.setCreditCardNumber("");
        creditCardDetailsResponseDTO.setType(creditCard);
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
