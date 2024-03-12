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
@Table(name = "az_transaction_bk")
public class Transaction {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(unique = true, nullable = false)
    private String txnReferenceId;

    private String responseTxnReferenceId;
    private String txnStatus;
    private String uniqueKey;
    private String txnDate;
    private String errorResponse;
    private String warningResponse;
}

