package com.backoffice.operations.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SecuritySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean biometricEnabled;
    private boolean touchIdEnabled;
    private boolean passcodeEnabled;
    private boolean pinEnabled;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
