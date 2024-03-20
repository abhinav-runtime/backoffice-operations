package com.backoffice.operations.entity;

import org.hibernate.annotations.GenericGenerator;

import com.backoffice.operations.enums.TransferType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "az_transaction_code_bk")
public class TransactionCode {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	@Enumerated
	TransferType transferType;
	@Column(name = "chargeType")
	private String chargeType;
	@Column(name = "cbsProduct")
	private String cbsProduct;
	@Column(name = "cbsModule")
	private String cbsModule;
	@Column(name = "cbsNetwork")
	private String cbsNetwork;
}
