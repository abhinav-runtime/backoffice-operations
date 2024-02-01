package com.backoffice.operations.payloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalApiResponseDTO {
    private Result result;
    private String exception;
    private Object pagination;

    public Result getResult() {
		return result;
	}


	public void setResult(Result result) {
		this.result = result;
	}


	public String getException() {
		return exception;
	}


	public void setException(String exception) {
		this.exception = exception;
	}


	public Object getPagination() {
		return pagination;
	}


	public void setPagination(Object pagination) {
		this.pagination = pagination;
	}


	public static class Result {
        private String aliasName;
        private List<String> blockCode;
        private String dob;
        private String name;
        private String entityId;
        private List<Card> cardList;

        public String getAliasName() {
			return aliasName;
		}

		public void setAliasName(String aliasName) {
			this.aliasName = aliasName;
		}

		public List<String> getBlockCode() {
			return blockCode;
		}

		public void setBlockCode(List<String> blockCode) {
			this.blockCode = blockCode;
		}

		public String getDob() {
			return dob;
		}

		public void setDob(String dob) {
			this.dob = dob;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public List<Card> getCardList() {
			return cardList;
		}

		public void setCardList(List<Card> cardList) {
			this.cardList = cardList;
		}

		public static class Card {
            private String kitNo;
            private String bin;
            private String cardIssueDate;
            private String cardType;
            private String description;
            private String cardNo;
            private String awbNumber;
            private String expiryDate;
            private String isPinSet;
            private String productCode;
            private String deliveryVendor;
            private String networkType;
            private String status;
            private String kitActivationDate;
			public String getKitNo() {
				return kitNo;
			}
			public void setKitNo(String kitNo) {
				this.kitNo = kitNo;
			}
			public String getBin() {
				return bin;
			}
			public void setBin(String bin) {
				this.bin = bin;
			}
			public String getCardIssueDate() {
				return cardIssueDate;
			}
			public void setCardIssueDate(String cardIssueDate) {
				this.cardIssueDate = cardIssueDate;
			}
			public String getCardType() {
				return cardType;
			}
			public void setCardType(String cardType) {
				this.cardType = cardType;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
			public String getCardNo() {
				return cardNo;
			}
			public void setCardNo(String cardNo) {
				this.cardNo = cardNo;
			}
			public String getAwbNumber() {
				return awbNumber;
			}
			public void setAwbNumber(String awbNumber) {
				this.awbNumber = awbNumber;
			}
			public String getExpiryDate() {
				return expiryDate;
			}
			public void setExpiryDate(String expiryDate) {
				this.expiryDate = expiryDate;
			}
			public String getIsPinSet() {
				return isPinSet;
			}
			public void setIsPinSet(String isPinSet) {
				this.isPinSet = isPinSet;
			}
			public String getProductCode() {
				return productCode;
			}
			public void setProductCode(String productCode) {
				this.productCode = productCode;
			}
			public String getDeliveryVendor() {
				return deliveryVendor;
			}
			public void setDeliveryVendor(String deliveryVendor) {
				this.deliveryVendor = deliveryVendor;
			}
			public String getNetworkType() {
				return networkType;
			}
			public void setNetworkType(String networkType) {
				this.networkType = networkType;
			}
			public String getStatus() {
				return status;
			}
			public void setStatus(String status) {
				this.status = status;
			}
			public String getKitActivationDate() {
				return kitActivationDate;
			}
			public void setKitActivationDate(String kitActivationDate) {
				this.kitActivationDate = kitActivationDate;
			}

        }
    }
}
