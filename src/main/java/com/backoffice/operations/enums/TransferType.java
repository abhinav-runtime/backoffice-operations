package com.backoffice.operations.enums;

public enum TransferType {
	SELF("Own (within own account)", "Between accounts"),
	ALIZZ_TRANSFER("Domestic", "Domestic ACH to Alizz"),
	ACH_TRANSFER("Domestic", "Domestic ACH to other banks");

	private final String globalType;
	private final String subType;

	TransferType(String globalType, String subType) {
		this.globalType = globalType;
		this.subType = subType;
	}

	public String getGlobalType() {
		return globalType;
	}

	public String getSubType() {
		return subType;
	}

	public static TransferType fromString(String text) {
		for (TransferType transferType : TransferType.values()) {
			if (transferType.name().equalsIgnoreCase(text)) {
				return transferType;
			}
		}
		return null;
	}

	public String[] getCategoryAndDescription(String transferType) {
		TransferType type = fromString(transferType);
		if (type != null) {
			return new String[]{type.getGlobalType(), type.getSubType()};
		}
		return null;
	}
}
