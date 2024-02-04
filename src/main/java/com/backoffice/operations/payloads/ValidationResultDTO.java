package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ValidationResult {
		private String message;
		private String civilID;

		public ValidationResult(String message, String civilID) {
			this.message = message;
			this.civilID = civilID;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getCivilID() {
			return civilID;
		}

		public void setCivilID(String civilID) {
			this.civilID = civilID;
		}
	}
}
