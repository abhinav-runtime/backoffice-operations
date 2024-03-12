package com.backoffice.operations.enums;

public enum JointTypeEnum {

    S("Single"),
    J("Joint"),
    D("Dormant");

    private final String value;

    JointTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getValueByCode(String code) {
        for (JointTypeEnum accountType : values()) {
            if (accountType.name().equals(code)) {
                return accountType.getValue();
            }
        }
        return null;
    }
}
