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
	String id;
	@Column
	String globalLimit;
	@Column
	String segment;
	@Column
	long minPerTrxnAmt;
	@Column
	long maxPerTrxnAmt;
	@Column
	long dailyAmt;
	@Column
	long monthlyAmt;
	@Column
	long dailyCount;
	@Column
	long monthlyCount;
}
