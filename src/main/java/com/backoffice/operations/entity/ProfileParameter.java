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
@Table(name = "az_profile_parameter_bk")
public class ProfileParameter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@Column(name = "mobile_change_attempt_limit", nullable = false)
	private int mobileChangeAttemptLimit;
	@Column(name = "mobile_number_verification_link_resend_attempts_in_sec", nullable = false)
	private int mobileChangeLockoutDurationInSec;
	@Column(name = "mobile_number_verification_link_timeout_in_Day", nullable = false)
	private int mobileNumberChangedLockoutDurationInDay;
	@Column(name = "mobile_number_changed_duration", nullable = false)
	private int emailIdChangeAttemptLimit;
	@Column(name = "email_id_verification_link_resend_attempts", nullable = false)
	private int emailIdVerificationLinkResendAttempts;
	@Column(name = "email_id_verification_link_timeout_in_sec", nullable = false)
	private int emailIdVerificationLinkTimeoutInSec;
	@Column(name = "email_id_change_lockout_duration_in_sec", nullable = false)
	private int emailIdChangeLockoutDurationInSec;
	@Column(name = "email_id_changed_duration_in_day", nullable = false)
	private int emailIdChangedDurationInDay;
	@Column(name = "email_statement_flag", nullable = false)
	private int emailStatementFlagAttempts;
}
