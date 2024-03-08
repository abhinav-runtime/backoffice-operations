package com.backoffice.operations.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlizzTransferDto {

    @JsonProperty("transaction")
    private Transaction transaction;

    @JsonProperty("sender")
    private Sender sender;

    @JsonProperty("receiver")
    private Receiver receiver;

    @JsonProperty("header")
    private Header header;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Transaction {

        @JsonProperty("payment_details_1")
        private String paymentDetails1;
        @JsonProperty("payment_details_2")
        private String paymentDetails2;
        @JsonProperty("transaction_date")
        private String transactionDate;
        @JsonProperty("transaction_amount")
        private Double transactionAmount;
        @JsonProperty("transaction_purpose")
        private String transactionPurpose;
        @JsonProperty("transaction_currency")
        private String transactionCurrency;
        @JsonProperty("transaction_reference")
        private String transactionReference;
        @JsonProperty("charge_type")
        private String chargeType;
        @JsonProperty("cbs_product")
        private String cbsProduct;
        @JsonProperty("cbs_module")
        private String cbsModule;
        @JsonProperty("cbs_network")
        private String cbsNetwork;
        @JsonProperty("payment_details_3")
        private String paymentDetails3;
        @JsonProperty("payment_details_4")
        private String paymentDetails4;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Sender {
        @JsonProperty("account_name")
        private String accountName;

        @JsonProperty("branch_code")
        private String branchCode;

        @JsonProperty("account_number")
        private String accountNumber;

        @JsonProperty("account_currency")
        private String accountCurrency;

        @JsonProperty("bank_code")
        private String bankCode;

        @JsonProperty("bank_name")
        private String bankName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Receiver {

        @JsonProperty("account_number")
        private String accountNumber;

        @JsonProperty("account_name")
        private String accountName;

        @JsonProperty("notes_to_receiver")
        private String notesToReceiver;

        @JsonProperty("bank_code")
        private String bankCode;
        @JsonProperty("bank_name")
        private String bankName;
        @JsonProperty("branch_code")
        private String branchCode;
        @JsonProperty("iban_account_number")
        private String iBanAccountNumber;
        @JsonProperty("bank_address1")
        private String bankAddress1;
        @JsonProperty("bank_address2")
        private String bankAddress2;
        @JsonProperty("bank_address3")
        private String bankAddress3;
        @JsonProperty("bank_address4")
        private String bankAddress4;
        @JsonProperty("bene_address1")
        private String beneAddress1;
        @JsonProperty("bene_address2")
        private String beneAddress2;
        @JsonProperty("bene_address3")
        private String beneAddress3;
        @JsonProperty("bene_address4")
        private String beneAddress4;
        @JsonProperty("bank_country")
        private String bankCountry;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header {
        private String source_system;
        private String source_user;
        private String source_operation;
    }
    }