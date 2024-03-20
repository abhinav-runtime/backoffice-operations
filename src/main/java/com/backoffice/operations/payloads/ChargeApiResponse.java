package com.backoffice.operations.payloads;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Getter
@Setter
public class ChargeApiResponse {
    private boolean success;
    private String message;
    private List<ChargeSheetEntry> chargeSheet;

    @Data
    public static class ChargeSheetEntry {
        private String chargeComponent;
        private String chargeDescription;
        private String chargeCurrency;
        private int chargeAmount;
        private String chargeStatus;
    }
}