package com.backoffice.operations.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "az_annexure_transfer_limits_bk")
public class AnnexureTransferLimits {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column
    private String globalLimit;
    @Column
    private String segment;
    @Column
    private long minPerTrxnAmt;
    @Column
    private long maxPerTrxnAmt;
    @Column
    private long dailyAmt;
    @Column
    private long monthlyAmt;
    @Column
    private long dailyCount;
    @Column
    private long monthlyCount;
}

