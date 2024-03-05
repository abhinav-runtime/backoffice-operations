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
                private List<UpcomingEvents> upcomingEvents;
                private List<AmountBlocks> amountBlocks;

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
                    private String statsince;
                    private String brn;
                    private double acyblkamt;

                }

                @Getter
                @Setter
                public static class Istddetails{

                    private String custacno;
                    private String ccy;
                    private String acdesc;
                    private String acctype;
                    private double tdamt;
                    private String accclass;
                    private String branch;
                    private String statsince;

                }

                @Getter
                @Setter
                public static class UpcomingEvents{
                        public String event;
                        public String branch;
                        public String refno;
                        public double amount;
                        public String eventdt;
                        public String ccy;
                }

                @Getter
                @Setter
                public static class AmountBlocks {
                    private String branch;
                    private String account;
                    private String amtblkno;
                    private double amount;
                    private String effdate;
                    private String expdate;
                    private String blktype;
                }
            }
        }
    }
}
