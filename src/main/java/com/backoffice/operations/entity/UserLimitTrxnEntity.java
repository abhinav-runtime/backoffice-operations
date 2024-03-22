package com.backoffice.operations.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "az_user_limit_trxnbk")
public class UserLimitTrxnEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String uniqueKey;
    private String accountNumber;
    private Double dailyTrxnLimit;
    private Double monthlyTrxnLimit;
    private Integer dailyTrxnCount;
    private Integer monthlyTrxnCount;
}
