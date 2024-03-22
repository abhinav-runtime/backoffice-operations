package com.backoffice.operations.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
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
