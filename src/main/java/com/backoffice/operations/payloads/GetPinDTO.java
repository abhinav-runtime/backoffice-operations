package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPinDTO {

	private String uniqueKeyCivilId;
	private String pin;

	public String getUniqueKeyCivilId() {
		return uniqueKeyCivilId;
	}

	public void setUniqueKeyCivilId(String uniqueKeyCivilId) {
		this.uniqueKeyCivilId = uniqueKeyCivilId;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

}
