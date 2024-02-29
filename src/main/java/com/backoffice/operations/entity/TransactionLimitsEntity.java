package com.backoffice.operations.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "az_transaction_limits_bk")
public class TransactionLimitsEntity {
	
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;
	@Column(name= "customerId")
	private String customerId;
	@Column (name="merchantOutletLimits")
	private double merchantOutletLimits;
	@Column (name="onlineShoppingLimits")
	private double onlineShoppingLimits;
	@Column (name="atmWithdrawalLimits")
	private double atmWithdrawalLimits;
	@Column (name="tapAndPayLimits")
	private double tapAndPayLimits;
	
	//Getter and Setter
}
