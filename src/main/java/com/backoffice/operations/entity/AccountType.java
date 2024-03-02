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
@Table(name = "az_account_type_bk", uniqueConstraints = {@UniqueConstraint(columnNames = {"cbsProductCode"})})
public class AccountType {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    private String accountType;
    private String description;
    private String cbsProductCode;
    private String requestDebitCard;
    private String requestsChequeBook;
    private String billPayments;
    private String transfers;
    private String editAccountInfo;
    private String visibility;
    private String accountNickName;
}
