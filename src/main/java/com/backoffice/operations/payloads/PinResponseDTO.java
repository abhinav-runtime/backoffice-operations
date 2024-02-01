package com.backoffice.operations.payloads;

public class PinResponseDTO {
	
	private PinResult result;
    private String exception;
    private String pagination;

    public PinResult getResult() {
		return result;
	}

	public void setResult(PinResult result) {
		this.result = result;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getPagination() {
		return pagination;
	}

	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

	public static class PinResult {
        private boolean status;

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

    }

}
