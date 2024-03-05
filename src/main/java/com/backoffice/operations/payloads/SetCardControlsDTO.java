package com.backoffice.operations.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetCardControlsDTO {
	
	@NotBlank(message = "Unique Key cannot be blank")
	private String uniqueKey;
	private boolean posFlag;
	private boolean ecomFlag;
	private boolean atmFlag;
	private boolean contactlessFlag;
	
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public boolean isPosFlag() {
		return posFlag;
	}
	public void setPosFlag(boolean posFlag) {
		this.posFlag = posFlag;
	}
	public boolean isEcomFlag() {
		return ecomFlag;
	}
	public void setEcomFlag(boolean ecomFlag) {
		this.ecomFlag = ecomFlag;
	}
	public boolean isAtmFlag() {
		return atmFlag;
	}
	public void setAtmFlag(boolean atmFlag) {
		this.atmFlag = atmFlag;
	}
	public boolean isContactlessFlag() {
		return contactlessFlag;
	}
	public void setContactlessFlag(boolean contactlessFlag) {
		this.contactlessFlag = contactlessFlag;
	}	
}
