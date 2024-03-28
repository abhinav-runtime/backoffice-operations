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
@Table(name = "az_otp_entity_bk")
public class OtpEntity {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	private String uniqueKeyCivilId;
	private String otp;
	private int attempts;
	@Column(columnDefinition = "boolean default false")
	private boolean transferWithinAlizzValidate;
	private LocalDateTime lastAttemptTime;
	@Column(columnDefinition = "int default 0")
	private int resendAttempts;
	@Column(nullable = true)
	private LocalDateTime lastResendTime;


	public String getUniqueKeyCivilId() {
		return uniqueKeyCivilId;
	}

	public void setUniqueKeyCivilId(String uniqueKeyCivilId) {
		this.uniqueKeyCivilId = uniqueKeyCivilId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public LocalDateTime getLastAttemptTime() {
		return lastAttemptTime;
	}

	public void setLastAttemptTime(LocalDateTime lastAttemptTime) {
		this.lastAttemptTime = lastAttemptTime;
	}

}
