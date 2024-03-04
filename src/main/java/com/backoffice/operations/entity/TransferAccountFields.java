package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Table(name = "az_transfer_account_fields_bk")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferAccountFields {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column(nullable = false, unique = true)
    private String transferType;

    @Column(nullable = false, unique = false)
    private String chargeType;

    @Column(nullable = false, unique = false)
    private String cbsProduct;

    @Column(nullable = false, unique = false)
    private String cbsModule;

    @Column(nullable = false, unique = false)
    private String cbsNetwork;

}
