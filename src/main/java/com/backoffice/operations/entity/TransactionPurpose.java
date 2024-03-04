package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Table(name = "az_transaction_purpose_bk")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPurpose {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column(nullable = false, unique = true)
    private String transactionPurposeCode;

    @Column(nullable = false, unique = true)
    private String transactionPurpose;

}
