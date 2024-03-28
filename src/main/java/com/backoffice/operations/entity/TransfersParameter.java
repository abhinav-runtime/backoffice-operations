package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Table(name = "az_transfers_parameter_bk")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransfersParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "beneficiary_account_number_min_chars_alizz", nullable = false)
    private String beneficiaryAccountNumberMinCharsAlizz;

    @Column(name = "beneficiary_account_number_max_chars_alizz", nullable = false)
    private String beneficiaryAccountNumberMaxCharsAlizz;

    @Column(name = "beneficiary_account_number_min_chars_domestic", nullable = false)
    private String beneficiaryAccountNumberMinCharsDomestic;

    @Column(name = "beneficiary_account_number_max_chars_domestic", nullable = false)
    private String beneficiaryAccountNumberMaxCharsDomestic;

    @Column(name = "type_of_chars_allowed_in_beneficiary_account_number", nullable = false)
    private String typeOfCharsAllowedInBeneficiaryAccountNumber;

    @Column(name = "beneficiary_name_max_chars", nullable = false)
    private String beneficiaryNameMaxChars;

    @Column(name = "type_of_characters_allowed_in_beneficiary_name", nullable = false)
    private String typeOfCharactersAllowedInBeneficiaryName;

    @Column(name = "beneficiary_name_verification_within_alizz", nullable = false)
    private String beneficiaryNameVerificationWithinAlizz;

    @Column(name = "amount_limit_to_use_ach", nullable = false)
    private String amountLimitToUseACH;

    @Column(name = "ach_max_in_domestic_transfer", nullable = false)
    private String achMaxInDomesticTransfer;

    @Column(name = "transfer_note_max_chars", nullable = false)
    private String transferNoteMaxChars;

    @Column(name = "allowed_characters_in_transfer_note", nullable = false)
    private String allowedCharactersInTransferNote;

    @Column(name = "minimum_amount_transfer", nullable = false)
    private String minimumAmountTransfer;

    @Column(name = "transfer_domestic_timeout", nullable = false)
    private String transferDomesticTimeout;

    @Column(name = "transfer_otp_expiry", nullable = false)
    private String transferOTPExpiry;

    @Column(name = "transfer_otp_resends", nullable = false)
    private String transferOTPResends;

    @Column(name = "transfer_otp_attempts", nullable = false)
    private String transferOTPAttempts;

    @Column(name = "transfer_timeout", nullable = false)
    private String transferTimeout;

    @Column(name = "transfer_time_to_check_transaction_status", nullable = false)
    private String transferTimeToCheckTransactionStatus;

    @Column(name = "attempts_to_trigger_transaction_status", nullable = false)
    private String attemptsToTriggerTransactionStatus;

}