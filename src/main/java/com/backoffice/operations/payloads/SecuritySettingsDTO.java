package com.backoffice.operations.payloads;

import jakarta.validation.constraints.NotBlank;

public class SecuritySettingsDTO {
	
	private boolean biometricEnabled;
    private boolean touchIdEnabled;
    private boolean passcodeEnabled;
    private boolean pinEnabled;
    
    @NotBlank(message = "Unique Key cannot be blank")
	private String uniqueKey;

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
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
