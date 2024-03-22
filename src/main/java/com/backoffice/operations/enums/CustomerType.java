package com.backoffice.operations.enums;

public enum CustomerType {
	I("Mass - General"),
	D("Tharwa");
	private final String segment;

	CustomerType(String segment) {
		this.segment = segment;
	}

	public String getSegment() {
		return segment;
	}

	public static CustomerType fromString(String text) {
		for (CustomerType customerType : CustomerType.values()) {
			if (customerType.name().equalsIgnoreCase(text)) {
				return customerType;
			}
		}
		return null;
	}
}
