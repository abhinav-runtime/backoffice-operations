package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
	private String setPinKey;
    private String kitNo;
    private String pin;

    // Constructors, getters, and setters

    public CardDTO() {
    }

    public CardDTO(String setPinKey, String kitNo, String pin) {
        this.setPinKey = setPinKey;
        this.kitNo = kitNo;
        this.pin = pin;
    }

    public String getSetPinKey() {
        return setPinKey;
    }

    public void setSetPinKey(String setPinKey) {
        this.setPinKey = setPinKey;
    }

    public String getKitNo() {
        return kitNo;
    }

    public void setKitNo(String kitNo) {
        this.kitNo = kitNo;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
