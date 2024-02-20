package com.backoffice.operations.payloads;

public class ValidationResultDTO {
	private String status;
	private Data data;
	private String message;

	public ValidationResultDTO() {

	}

	public ValidationResultDTO(String status, Data data, String message) {
		super();
		this.status = status;
		this.data = data;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static class Data {
		private String uniqueKey;

		public Data() {

		}

		public Data(String uniqueKey) {
			super();
			this.uniqueKey = uniqueKey;
		}

		public String getUniqueKey() {
			return uniqueKey;
		}

		public void setUniqueKey(String uniqueKey) {
			this.uniqueKey = uniqueKey;
		}
	}
	
	
    
}