package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasscodeDto {
	
	private int passcode;

	public int getPasscode() {
		return passcode;
	}

	public void setPasscode(int passcode) {
		this.passcode = passcode;
	}

}
