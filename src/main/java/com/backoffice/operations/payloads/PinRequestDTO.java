package com.backoffice.operations.payloads;

public class PinRequestDTO {
	private String setPinKey;
    private String kitNo;
    private String pin;
    
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getSetPinKey() {
		return setPinKey;
	}
	public void setSetPinKey(String setPinKey) {
		this.setPinKey = setPinKey;
	}
	public String getKitNo() {
		return kitNo;
	}
	public void setKitNo(String kitNo) {
		this.kitNo = kitNo;
	}
    
}
