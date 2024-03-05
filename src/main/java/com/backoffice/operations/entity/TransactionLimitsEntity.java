package com.backoffice.operations.entity;

import java.time.LocalDateTime;

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
	@Column(name= "uniqueKey")
	private String uniqueKey;
	@Column (name="merchantOutletLimits")
	private double merchantOutletLimits;
	@Column (name="onlineShoppingLimits")
	private double onlineShoppingLimits;
	@Column (name="atmWithdrawalLimits")
	private double atmWithdrawalLimits;
	@Column (name="tapAndPayLimits")
	private double tapAndPayLimits;
	@Column (name="dateModified")
	private LocalDateTime dateModified;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getMerchantOutletLimits() {
		return merchantOutletLimits;
	}
	public void setMerchantOutletLimits(double merchantOutletLimits) {
		this.merchantOutletLimits = merchantOutletLimits;
	}
	public double getOnlineShoppingLimits() {
		return onlineShoppingLimits;
	}
	public void setOnlineShoppingLimits(double onlineShoppingLimits) {
		this.onlineShoppingLimits = onlineShoppingLimits;
	}
	public double getAtmWithdrawalLimits() {
		return atmWithdrawalLimits;
	}
	public void setAtmWithdrawalLimits(double atmWithdrawalLimits) {
		this.atmWithdrawalLimits = atmWithdrawalLimits;
	}
	public double getTapAndPayLimits() {
		return tapAndPayLimits;
	}
	public void setTapAndPayLimits(double tapAndPayLimits) {
		this.tapAndPayLimits = tapAndPayLimits;
	}
	public LocalDateTime getDateModified() {
		return dateModified;
	}
	public void setDateModified(LocalDateTime dateModified) {
		this.dateModified = dateModified;
	}
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	
	//Getter and Setter
	
}
