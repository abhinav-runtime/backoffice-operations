package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransfersParameterDTO {

    private Long id;
    private String beneficiaryAccountNumberMinCharsAlizz;
    private String beneficiaryAccountNumberMaxCharsAlizz;
    private String beneficiaryAccountNumberMinCharsDomestic;
    private String beneficiaryAccountNumberMaxCharsDomestic;
    private String typeOfCharsAllowedInBeneficiaryAccountNumber;
    private String beneficiaryNameMaxChars;
    private String typeOfCharactersAllowedInBeneficiaryName;
    private String beneficiaryNameVerificationWithinAlizz;
    private String amountLimitToUseACH;
    private String achMaxInDomesticTransfer;
    private String transferNoteMaxChars;
    private String allowedCharactersInTransferNote;
    private String minimumAmountTransfer;
    private String transferDomesticTimeout;
    private String transferOTPExpiry;
    private String transferOTPResends;
    private String transferOTPAttempts;
    private String transferTimeout;
    private String transferTimeToCheckTransactionStatus;
    private String attemptsToTriggerTransactionStatus;
}