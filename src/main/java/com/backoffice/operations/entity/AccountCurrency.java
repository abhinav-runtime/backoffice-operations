package com.backoffice.operations.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Table(name = "az_account_currency_bk")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCurrency {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column(nullable = false, unique = true)
    private String accountCurrencyCode;

    @Column(nullable = false, unique = true)
    private String accountCurrency;

}
