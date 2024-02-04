package com.backoffice.operations.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "az_security_settings_bk")
public class SecuritySettings {

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    private boolean biometricEnabled;
    private boolean touchIdEnabled;
    private boolean passcodeEnabled;
    private boolean pinEnabled;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isBiometricEnabled() {
		return biometricEnabled;
	}
	public void setBiometricEnabled(boolean biometricEnabled) {
		this.biometricEnabled = biometricEnabled;
	}
	public boolean isTouchIdEnabled() {
		return touchIdEnabled;
	}
	public void setTouchIdEnabled(boolean touchIdEnabled) {
		this.touchIdEnabled = touchIdEnabled;
	}
	public boolean isPasscodeEnabled() {
		return passcodeEnabled;
	}
	public void setPasscodeEnabled(boolean passcodeEnabled) {
		this.passcodeEnabled = passcodeEnabled;
	}
	public boolean isPinEnabled() {
		return pinEnabled;
	}
	public void setPinEnabled(boolean pinEnabled) {
		this.pinEnabled = pinEnabled;
	}

}
