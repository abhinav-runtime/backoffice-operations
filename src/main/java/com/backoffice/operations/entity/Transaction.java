package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "az_transaction_bk")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String txnReferenceId;

    private String responseTxnReferenceId;
    private String txnStatus;
    private String uniqueKey;
    private String txnDate;
    private String errorResponse;
}

