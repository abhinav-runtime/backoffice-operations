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
@Table(name = "az_beneficiary_bank_bk", uniqueConstraints = {@UniqueConstraint(columnNames = {"bankCode"})})
public class BeneficiaryBank {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    private String bankName;
    private String bankCode;
}
