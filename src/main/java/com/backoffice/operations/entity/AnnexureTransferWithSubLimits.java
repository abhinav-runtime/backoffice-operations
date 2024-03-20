package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "az_annexure_transfer_with_sub_limits_bk")
public class AnnexureTransferWithSubLimits {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column
    private String subTypeLimit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "annexure_transfer_limits_id", referencedColumnName = "id")
    private AnnexureTransferLimits annexureTransferLimits;

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
