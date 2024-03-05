package com.backoffice.operations.payloads;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BlockedAmountResponseDto {

    private String branch;
    private String account;
    private String amtblkno;
    private double amount;
    private String effdate;
    private String expdate;
    private String blktype;
}
