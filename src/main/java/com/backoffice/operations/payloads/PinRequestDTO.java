package com.backoffice.operations.payloads;

public class PinRequestDTO {
	private String entityId;
    private String pin;
    private String kitNo;
    private String expiryDate;
    private String dob;

    public PinRequestDTO() {
    }

    public PinRequestDTO(String entityId, String pin, String kitNo, String expiryDate, String dob) {
        this.entityId = entityId;
        this.pin = pin;
        this.kitNo = kitNo;
        this.expiryDate = expiryDate;
        this.dob = dob;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getKitNo() {
        return kitNo;
    }

    public void setKitNo(String kitNo) {
        this.kitNo = kitNo;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    
}
