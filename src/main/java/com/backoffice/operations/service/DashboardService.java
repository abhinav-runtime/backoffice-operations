package com.backoffice.operations.service;

import com.backoffice.operations.payloads.DashboardDto;
import com.backoffice.operations.payloads.DashboardInfoDto;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    DashboardDto getDashboardDetails(String uniqueKey);

    DashboardInfoDto getDashboardInfo(String accountNumber);
}
