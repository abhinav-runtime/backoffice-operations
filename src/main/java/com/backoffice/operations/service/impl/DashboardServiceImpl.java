package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.entity.DashboardEntity;
import com.backoffice.operations.entity.DashboardInfoEntity;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.DashboardInfoRepository;
import com.backoffice.operations.repository.DashboardRepository;
import com.backoffice.operations.service.DashboardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Value("${external.api.accounts}")
    private String accountIdExternalAPI;

    @Value("${external.api.accounts.balance}")
    private String accountBalanceExternalAPI;

    @Value("${external.api.url}")
    private String externalApiUrl;

    private final RestTemplate restTemplate;

    private final CivilIdRepository civilIdRepository;

    private final DashboardRepository dashboardRepository;

    private final DashboardInfoRepository dashboardInfoRepository;

    public DashboardServiceImpl(RestTemplate restTemplate, CivilIdRepository civilIdRepository, DashboardRepository dashboardRepository, DashboardInfoRepository dashboardInfoRepository) {
        this.restTemplate = restTemplate;
        this.civilIdRepository = civilIdRepository;
        this.dashboardRepository = dashboardRepository;
        this.dashboardInfoRepository = dashboardInfoRepository;
    }

    @Override
    public GenericResponseDTO<Object> getDashboardDetails(String uniqueKey) {

        Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(uniqueKey);

        if(civilIdEntity.isPresent()) {
            String apiUrl = accountIdExternalAPI + civilIdEntity.get().getCivilId();
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
    public GenericResponseDTO<Object> getDashboardInfo(String accountNumber, String uniqueKey) {
        String apiUrl = accountBalanceExternalAPI + accountNumber;
        ResponseEntity<AccountBalanceResponse> responseEntity = restTemplate.getForEntity(apiUrl,
                AccountBalanceResponse.class);
        AccountBalanceResponse apiResponse = responseEntity.getBody();

        Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(uniqueKey);
        if(civilIdEntity.isPresent()) {

            String cardListUrl = externalApiUrl + "/getCardList";
            HttpHeaders headers = new HttpHeaders();
            headers.add("TENANT", "ALIZZ_UAT");
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestBody = "{ \"customerId\": \"" + civilIdEntity.get().getCivilId() + "\" }";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<ExternalApiResponseDTO> cardListresponseEntity = restTemplate.exchange(cardListUrl,
                    HttpMethod.POST, requestEntity, ExternalApiResponseDTO.class);

            if (apiResponse != null && apiResponse.isSuccess()) {
                AccountBalanceResponse.Response.Payload.AccBalance.AccBal accBal = apiResponse.getResponse().getPayload().getAccbalance().getAccbal().get(0);
                DashboardInfoEntity dashboardInfoEntity = DashboardInfoEntity.builder().customerNumber(accBal.getCustacno())
                        .outstandingBal(accBal.getCurbal()).availableBal(accBal.getAvlbal()).build();
                DashboardInfoEntity dashboardInfoEntityObj = dashboardInfoRepository.save(dashboardInfoEntity);
                List<CreditCardDetailsResponseDTO> creditCardDetailsResponseList = new ArrayList<>();
                if (cardListresponseEntity != null && cardListresponseEntity.getBody().getResult() != null) {
                    String customerName = cardListresponseEntity.getBody().getResult().getName();
                    List<ExternalApiResponseDTO.Result.Card> cardList = cardListresponseEntity.getBody().getResult().getCardList();
                    for (ExternalApiResponseDTO.Result.Card card : cardList) {
                        String actualFirstFourDigits = card.getCardNo().substring(0, 4);
                        String actualLastFourDigits = card.getCardNo().substring(card.getCardNo().length() - 4);
                        CreditCardDetailsResponseDTO creditCardDetailsResponseDTO = new CreditCardDetailsResponseDTO();
                        creditCardDetailsResponseDTO.setAvailableBalance(dashboardInfoEntityObj.getAvailableBal());
                        creditCardDetailsResponseDTO.setOutstandingBalance(dashboardInfoEntityObj.getOutstandingBal());
                        creditCardDetailsResponseDTO.setCustomerName(customerName);
                        creditCardDetailsResponseDTO.setCreditCardNumber(actualFirstFourDigits + "********" + actualLastFourDigits);
                        creditCardDetailsResponseList.add(creditCardDetailsResponseDTO);
                    }
                }
                
               GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                Map<String, Object> data = new HashMap<>();
                data.put("creditCardDetails",creditCardDetailsResponseList);
                data.put("uniqueKey",dashboardInfoEntityObj.getId());
                responseDTO.setStatus("Success");
                responseDTO.setMessage("Success");
                responseDTO.setData(data);
                return responseDTO;
             }

        }
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();
        data.put("uniqueKey",uniqueKey);
        responseDTO.setStatus("Success");
        responseDTO.setMessage("Success");
        responseDTO.setData(data);
        return responseDTO;
    }
}
