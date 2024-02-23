package com.backoffice.operations.payloads;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalApiFetchBalanceResponseDTO {

    private Result result;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Result {
        private String limitAvailable;
    }


}
