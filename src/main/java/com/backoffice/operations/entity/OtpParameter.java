package com.backoffice.operations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "az_otp_parameter_bk")
public class OtpParameter {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
	
	@Column(name = "otpMaxAttempts")
    private int otpMaxAttempts;
    
    @Column(name = "otpCooldownInMin")
    private int otpCooldownInMin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOtpMaxAttempts() {
		return otpMaxAttempts;
	}

	public void setOtpMaxAttempts(int otpMaxAttempts) {
		this.otpMaxAttempts = otpMaxAttempts;
	}

	public int getOtpCooldownInMin() {
		return otpCooldownInMin;
	}

	public void setOtpCooldownInMin(int otpCooldownInMIn) {
		this.otpCooldownInMin = otpCooldownInMIn;
	}

}
