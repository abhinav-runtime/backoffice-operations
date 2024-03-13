package com.backoffice.operations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "az_account_info_parameter_bk")
public class AccountInfoParameter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@Column(name = "nickNameMinLength", nullable = false)
	private int nickNameMinLength;
	@Column(name = "nickNameMaxLength", nullable = false)
	private int nickNameMaxLength;
	@Column(name = "noOfTimesUserCanChangeNickName", nullable = false)
	private int noOfTimesUserCanChangeNickName;
	@Column(name = "makeAccountVisibleAttempts", nullable = false)
	private int makeAccountVisibleAttempts;
	@Column(name = "alertOnTransactions", nullable = false)
	private int alertOnTransactions;
	@Column(name = "alertOnLowBalanceAttempts", nullable = false)
	private int alertOnLowBalanceAttempts;
}
