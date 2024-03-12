package com.backoffice.operations.entity;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "az_civilId_parameter_bk")
public class CivilIdParameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "civilIdMaxAttempts", nullable = false)
	private int civilIdMaxAttempts;

	@Column(name = "civilIdCooldownInSec", nullable = false)
	private int civilIdCooldownInSec;

	@Column(name = "overall_civil_id_retry_attempts", nullable = false)
	private int overrallMaxAttempts;

	@Column(name = "device_level_attempts", nullable = false)
	private int deviceAttempts;

	public Long getId() {
		return id;
	}
}
