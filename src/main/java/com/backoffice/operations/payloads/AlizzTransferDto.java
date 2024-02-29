package com.backoffice.operations.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    public static class Transaction {

        @JsonProperty("payment_details_1")
        private String paymentDetails1;
        @JsonProperty("payment_details_2")
        private String paymentDetails2;
        @JsonProperty("transaction_date")
        private String transactionDate;
        @JsonProperty("transaction_amount")
        private String transactionAmount;
        @JsonProperty("transaction_purpose")
        private String transactionPurpose;
        @JsonProperty("transaction_currency")
        private String transactionCurrency;
        @JsonProperty("transaction_reference")
        private String transaction_reference;
        @JsonProperty("charge_type")
        private String charge_type;
        @JsonProperty("cbs_product")
        private String cbs_product;
        @JsonProperty("cbs_module")
        private String cbs_module;
        @JsonProperty("cbs_network")
        private String cbs_network;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Sender {
        @JsonProperty("account_name")
        private String accountName;

        @JsonProperty("branch_code")
        private String branchCode;

        @JsonProperty("account_number")
        private String accountNumber;

        @JsonProperty("account_currency")
        private String accountCurrency;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Receiver {

        @JsonProperty("account_number")
        private String accountNumber;

        @JsonProperty("account_name")
        private String accountName;

        @JsonProperty("notes_to_receiver")
        private String notesToReceiver;
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