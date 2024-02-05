package com.backoffice.operations.payloads;

import jakarta.validation.constraints.NotBlank;

public class SecuritySettingsDTO {

	private boolean biometricEnabled;
	private boolean faceIdEnabled;
	private boolean passcodeEnabled;
	private boolean pinEnabled;
	@NotBlank(message = "Unique Key cannot be blank")
	private String uniqueKey;
	private String lang;

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
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

	public boolean isFaceIdEnabled() {
		return faceIdEnabled;
	}

	public void setFaceIdEnabled(boolean faceIdEnabled) {
		this.faceIdEnabled = faceIdEnabled;
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
