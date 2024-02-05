package com.backoffice.operations.payloads;

public class ValidationResultDTO {
	private ValidationResult result;

	public ValidationResultDTO(ValidationResult result) {
		this.result = result;
	}

	public ValidationResult getResult() {
		return result;
	}

	public void setResult(ValidationResult result) {
		this.result = result;
	}

	public static class ValidationResult {
		private String message;
		private String uniqueKey;

		public ValidationResult(String message, String uniqueKey) {
			this.message = message;
			this.uniqueKey = uniqueKey;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getUniqueKey() {
			return uniqueKey;
		}

		public void setUniqueKey(String uniqueKey) {
			this.uniqueKey = uniqueKey;
		}

	}
}
