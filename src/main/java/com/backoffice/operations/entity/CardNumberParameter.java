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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "az_card_number_parameter_bk")
public class CardNumberParameter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@Column(name = "card_first_digit_length", nullable = false)
	private int cardFirstDigitLength;
	@Column(name = "card_last_digit_length", nullable = false)
	private int cardLastDigitLength;
	@Column(name = "card_number_length", nullable = false)
	private int entryTimeOut;
	@Column(name = "scan_option", nullable = false)
	private Boolean scanOption;
	@Column(name = "input_retry_limit", nullable = false)
	private int inputRetryLimit;
	@Column(name = "session_expire_time", nullable = false)
	private int sessionExpireTime;
}
