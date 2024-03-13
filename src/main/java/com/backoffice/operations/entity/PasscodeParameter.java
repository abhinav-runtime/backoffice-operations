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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "az_passcode_parameter_bk")
public class PasscodeParameter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@Column(name = "passCodeLength", nullable = false)
	private int passcodeLength;
	@Column(name = "passCodeMaxAttempt", nullable = false)
	private int passCodeMaxAttempt;
	@Column(name = "lockoutDurationInMin", nullable = false)
	private int lockoutDurationInMin;
	@Column(name = "changePassCodeMaxAttempt", nullable = false)
	private int changePasscodeMaxAttempt;
	@Column(name = "changePasscodeDurationInDays", nullable = false)
	private int changePasscodeDurationInDays;
}
