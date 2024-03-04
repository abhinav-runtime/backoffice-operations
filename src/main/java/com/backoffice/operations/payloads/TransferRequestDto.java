package com.backoffice.operations.payloads;

import lombok.*;

@Data
public class TransferRequestDto {
    public Sender sender;
    private Header header;
    private Transaction transaction;
    private Receiver receiver;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Header {
        private String source_system;
        private String source_user;
        private String source_operation;

    }

    @Data
    public
    class Transaction {
        private String transaction_reference;
        private String transaction_date;
        private double transaction_amount;
        private String transaction_purpose;
        private String transaction_currency;
        private String cbs_product;
        private String cbs_module;
        private String cbs_network;
        private String charge_type;
        private String payment_details_1;
        private String payment_details_2;
        private String payment_details_3;
        private String payment_details_4;

    }

    @Data
    public
    class Sender {
        private String account_name;
        private String account_number;
        private String account_currency;
        private String bank_code;
        private String bank_name;
        private String branch_code;
    }

    @Data
    public
    class Receiver {
        private String account_number;
        private String account_name;
        private String notes_to_receiver;
        private String bank_code;
        private String bank_name;
        private String branch_code;
        private String bank_country;
        private String iban_account_number;
        private String bank_address1;
        private String bank_address2;
        private String bank_address3;
        private String bank_address4;
        private String bene_address1;
        private String bene_address2;
        private String bene_address3;
        private String bene_address4;
    }
}