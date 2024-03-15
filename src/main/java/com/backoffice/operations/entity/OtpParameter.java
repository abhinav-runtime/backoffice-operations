package com.backoffice.operations.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "az_otp_parameter_bk")
public class OtpParameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "otpValidity", nullable = false)
	private int otpValidity;
	@Column(name = "maxFailedOtpAttempts", nullable = false)
	private int otpMaxAttempts;
	@Column(name = "otpAttemptTimeoutInMin", nullable = false)
	private int attemptTimeOut;
//	@Column(name = "otpLanguage", nullable = false)
//	private Set<String> otpLang;
	@Column(name = "otpLength", nullable = false)
	private int otpLength;
//	@Column(name = "allowedChannels", nullable = false)
//	private Set<String> allowedChannels;
	@Column(name = "resendOtpCount", nullable = false)
	private int otpResend;
	@Column(name = "otpResendInterval", nullable = false)
	private int otpResentTime;
	@Column(name = "sessionExpiry", nullable = false)
	private int sessionExpiry;
}
