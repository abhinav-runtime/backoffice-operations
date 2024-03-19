package com.backoffice.operations.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "az_annexure_transfer_with_sub_limits_bk")
public class AnnexureTransferWithSubLimits extends DailyTrxnLimitAmt{

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column
	private String subTypeLimit;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "annexure_transfer_limits_id", referencedColumnName = "id")
	private AnnexureTransferLimits annexureTransferLimits;
}
