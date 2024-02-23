package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.entity.DashboardEntity;
import com.backoffice.operations.entity.DashboardInfoEntity;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.DashboardInfoRepository;
import com.backoffice.operations.repository.DashboardRepository;
import com.backoffice.operations.service.DashboardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Value("${external.api.accounts}")
    private String accountIdExternalAPI;

    @Value("${external.api.accounts.balance}")
    private String accountBalanceExternalAPI;

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
    public ValidationResultDTO getDashboardDetails(String uniqueKey) {

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
                ValidationResultDTO.Data data = new ValidationResultDTO.Data();
                data.setAccountBalance(dashboardEntityObject.getAvailableBalance());
                data.setCurrency(dashboardEntityObject.getCurrency());
                data.setAccountNumber(dashboardEntityObject.getAccountNumber());
                data.setUniqueKey(dashboardEntityObject.getId());
                return ValidationResultDTO.builder().status("Success").message("Success")
                        .data(data).build();
            }
        }
        ValidationResultDTO.Data data = new ValidationResultDTO.Data();
        data.setUniqueKey(uniqueKey);
        return  ValidationResultDTO.builder().status("Failure").message("No Entry Found").data(data).build();
    }

    @Override
    public ValidationResultDTO getDashboardInfo(String accountNumber, String uniqueKey) {
        String apiUrl = accountBalanceExternalAPI + accountNumber;
        ResponseEntity<AccountBalanceResponse> responseEntity = restTemplate.getForEntity(apiUrl,
                AccountBalanceResponse.class);
        AccountBalanceResponse apiResponse = responseEntity.getBody();

        if (apiResponse != null && apiResponse.isSuccess()) {
            AccountBalanceResponse.Response.Payload.AccBalance.AccBal accBal = apiResponse.getResponse().getPayload().getAccbalance().getAccbal().get(0);
            DashboardInfoEntity dashboardInfoEntity = DashboardInfoEntity.builder().customerNumber(accBal.getCustacno())
                    .outstandingBal(accBal.getCurbal()).availableBal(accBal.getAvlbal()).build();
            DashboardInfoEntity dashboardInfoEntityObj = dashboardInfoRepository.save(dashboardInfoEntity);

            ValidationResultDTO.Data data = new ValidationResultDTO.Data();
            data.setAvailableBalance(dashboardInfoEntityObj.getAvailableBal());
            data.setOutstandingBalance(dashboardInfoEntityObj.getOutstandingBal());
            data.setAccountNumber(dashboardInfoEntityObj.getCustomerNumber());
            data.setUniqueKey(dashboardInfoEntityObj.getId());

            return ValidationResultDTO.builder().status("Success").message("Success")
                    .data(data).build();
        }
        ValidationResultDTO.Data data = new ValidationResultDTO.Data();
        data.setUniqueKey(uniqueKey);
        return ValidationResultDTO.builder().status("Success").message("Success").data(data).build();
    }
}
