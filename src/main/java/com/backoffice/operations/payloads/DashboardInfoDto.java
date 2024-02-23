package com.backoffice.operations.payloads;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardInfoDto {

    private String accountNumber;
    private String accountName;
    private Double outstandingBalance;
    private Double availableBalance;
}
