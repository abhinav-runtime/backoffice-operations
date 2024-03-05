package com.backoffice.operations.payloads;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpcomingBillsResponseDto {

    private List<Events> eventsList;
    private double totalAmt;

    @Getter
    @Setter
    @Builder
    public static class Events{
        private String event;
        private double amount;
        private String currency;
        private String dueOn;
    }
}
