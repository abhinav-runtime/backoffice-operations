package com.backoffice.operations.payloads;

public class CardListResponse {

	private String message;
	private String otp;

	public CardListResponse(String message, String otp) {
		super();
		this.message = message;
		this.otp = otp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}
