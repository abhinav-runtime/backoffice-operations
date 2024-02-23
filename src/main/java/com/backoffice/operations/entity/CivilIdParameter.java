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
@Table(name = "az_civilId_parameter_bk")
public class CivilIdParameter {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
	
	@Column(name = "civilIdMaxAttempts")
    private int civilIdMaxAttempts;
    
    @Column(name = "civilIdCooldownInSec")
    private int civilIdCooldownInSec;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCivilIdMaxAttempts() {
		return civilIdMaxAttempts;
	}

	public void setCivilIdMaxAttempts(int civilIdMaxAttempts) {
		this.civilIdMaxAttempts = civilIdMaxAttempts;
	}

	public int getCivilIdCooldownInSec() {
		return civilIdCooldownInSec;
	}

	public void setCivilIdCooldownInSec(int civilIdCooldownInSec) {
		this.civilIdCooldownInSec = civilIdCooldownInSec;
	}

}
