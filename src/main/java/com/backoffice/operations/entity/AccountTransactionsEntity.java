package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "az_account_transactions_bk")
public class AccountTransactionsEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    private String accountNumber;
    private String transactionDescription;
    private Double transactionAmount;
    private LocalDate transactionDate;
    private String indicator;
    private String referenceNumber;

}
