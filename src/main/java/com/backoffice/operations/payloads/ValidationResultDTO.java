// package com.backoffice.operations.payloads;

// import com.fasterxml.jackson.annotation.JsonInclude;
// import lombok.*;

// @Getter
// @Setter
// @Builder
// @JsonInclude(JsonInclude.Include.NON_NULL)
// public class ValidationResultDTO {
// 	private String status;
// 	private Data data;
// 	private String message;

// 	public ValidationResultDTO() {

// 	}

// 	public ValidationResultDTO(String status, Data data, String message) {
// 		super();
// 		this.status = status;
// 		this.data = data;
// 		this.message = message;
// 	}

// 	public String getStatus() {
// 		return status;
// 	}

// 	public void setStatus(String status) {
// 		this.status = status;
// 	}

// 	public Data getData() {
// 		return data;
// 	}

// 	public void setData(Data data) {
// 		this.data = data;
// 	}

// 	public String getMessage() {
// 		return message;
// 	}

// 	public void setMessage(String message) {
// 		this.message = message;
// 	}

// 	@JsonInclude(JsonInclude.Include.NON_NULL)
// 	public static class Data {
// 		private String uniqueKey;
// 		private String entityId;
// 	    private String kitNo;
// 	    private String expiryDate;
// 	    private String dob;
// 		private String accountNumber;
// 		private String currency;
// 		private Double accountBalance;
// 		private String accountName;
// 		private Double outstandingBalance;
// 		private Double availableBalance;


// 		public Data() {

// 		}

// 		public String getEntityId() {
// 			return entityId;
// 		}

// 		public void setEntityId(String entityId) {
// 			this.entityId = entityId;
// 		}

// 		public String getKitNo() {
// 			return kitNo;
// 		}

// 		public void setKitNo(String kitNo) {
// 			this.kitNo = kitNo;
// 		}

// 		public String getExpiryDate() {
// 			return expiryDate;
// 		}

// 		public void setExpiryDate(String expiryDate) {
// 			this.expiryDate = expiryDate;
// 		}

// 		public String getDob() {
// 			return dob;
// 		}

// 		public void setDob(String dob) {
// 			this.dob = dob;
// 		}

// 		public String getUniqueKey() {
// 			return uniqueKey;
// 		}

// 		public void setUniqueKey(String uniqueKey) {
// 			this.uniqueKey = uniqueKey;
// 		}

// 		public String getAccountNumber() {
// 			return accountNumber;
// 		}

// 		public void setAccountNumber(String accountNumber) {
// 			this.accountNumber = accountNumber;
// 		}

// 		public String getCurrency() {
// 			return currency;
// 		}

// 		public void setCurrency(String currency) {
// 			this.currency = currency;
// 		}

// 		public Double getAccountBalance() {
// 			return accountBalance;
// 		}

// 		public void setAccountBalance(Double accountBalance) {
// 			this.accountBalance = accountBalance;
// 		}

// 		public String getAccountName() {
// 			return accountName;
// 		}

// 		public void setAccountName(String accountName) {
// 			this.accountName = accountName;
// 		}

// 		public Double getOutstandingBalance() {
// 			return outstandingBalance;
// 		}

// 		public void setOutstandingBalance(Double outstandingBalance) {
// 			this.outstandingBalance = outstandingBalance;
// 		}

// 		public Double getAvailableBalance() {
// 			return availableBalance;
// 		}

// 		public void setAvailableBalance(Double availableBalance) {
// 			this.availableBalance = availableBalance;
// 		}
// 	}
// }