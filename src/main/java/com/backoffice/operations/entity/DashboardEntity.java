package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "az_dashboard_bk", uniqueConstraints = {@UniqueConstraint(columnNames = {"accountNumber", "uniqueKey"})})
public class DashboardEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
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
    private String customerNickName;
    private String uniqueKey;
    private String requestDebitCard;
    private String requestsChequeBook;
    private String billPayments;
    private String transfers;
    private String editAccountInfo;
    private String visibility;
    private String color;
}
