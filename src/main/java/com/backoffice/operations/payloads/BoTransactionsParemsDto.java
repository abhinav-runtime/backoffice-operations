package com.backoffice.operations.payloads;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoTransactionsParemsDto {
	@Builder.Default
    long pageNo = 20;
    @Builder.Default
    long pageSize = 0;
	String fromDate;
	@Builder.Default
	String toDate = new Date(System.currentTimeMillis()).toString();
	String txnCategory;
}
