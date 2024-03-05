package com.backoffice.operations.payloads;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountDetails {
    private boolean success;
    private String message;
    private Response response;

    @Getter
    @Setter
    public static class Response {
        private Payload payload;
        private Object fcubserrorresp;
        private Object fcubswarningresp;

        // getters and setters

        @Getter
        @Setter
        public static class Payload {
            private CustSummaryDetails custSummaryDetails;

            @Getter
            @Setter
            public static class CustSummaryDetails {

                private List<IslamicAccount> islamicAccounts;
                private List<Istddetails> istddetails;

                @Getter
                @Setter
                public static class IslamicAccount {
                    private String acc;
                    //private String accdesc;
                    private String ccy;
                    private double currbal;
                    private double acyavlbal;
                    private String accls;
                    private String acctype;
                    private String adesc;

                }

                @Getter
                @Setter
                public static class Istddetails{

                    public String custacno;
                    public String ccy;
                    public String acdesc;
                    public String acctype;
                    public double tdamt;
                    public String accclass;
                }
            }
        }
    }
}
