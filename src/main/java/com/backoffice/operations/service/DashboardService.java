package com.backoffice.operations.service;

import com.backoffice.operations.payloads.ValidationResultDTO;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    ValidationResultDTO getDashboardDetails(String uniqueKey);

    ValidationResultDTO getDashboardInfo(String uniqueKey);
}
