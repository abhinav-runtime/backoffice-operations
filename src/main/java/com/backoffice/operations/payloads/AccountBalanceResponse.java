package com.backoffice.operations.payloads;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountBalanceResponse {
    private boolean success;
    private String message;
    private Response response;

    @Getter
    @Setter
    public static class Response {
        private Payload payload;

        @Getter
        @Setter
        public static class Payload {
            private AccBalance accbalance;

            @Getter
            @Setter
            public static class AccBalance {
                private List<AccBal> accbal;

                @Getter
                @Setter
                public static class AccBal {
                    private String custacno;
                    private double curbal;
                    private double avlbal;
                }
            }
        }
    }
}

