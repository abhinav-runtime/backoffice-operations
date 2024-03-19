package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleData {
    private String moduleCode;
    private String productCode;
    private String customerNumber;
    private String accountNumber;
    private String fromDate;
    private String toDate;
    private String chequeLeaves;
    private String transactionCurrency;
    private String transactionAmount;
    private int tenure;
    private String eventCode;
}