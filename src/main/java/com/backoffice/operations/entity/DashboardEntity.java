package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "az_dashboard_bk")
public class DashboardEntity {

    @Id
    private String id;
    private String accountNumber;
    private String currency;
    private Double availableBalance;
    private String accountType;
    private String accountCodeDesc;
    private String accountNickName;
    private String shariaContract;
    private String openDate;
    private String branchName;
    private String jointType;
    private String customerName;
    private double blockedAmount;
    private Double currentAccountBalance;
    private boolean isAccountVisible;
    private boolean isAlertOnTrnx;
    private boolean isAlertOnLowBal;
    private double lowBalLimit;
}
