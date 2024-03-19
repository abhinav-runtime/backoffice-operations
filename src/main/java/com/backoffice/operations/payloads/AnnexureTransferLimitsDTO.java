package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnexureTransferLimitsDTO {
	String id;
	String globalLimit;
	String segment;
	long minPerTrxnAmt;
	long maxPerTrxnAmt;
	long dailyAmt;
	long monthlyAmt;
	long dailyCount;
	long monthlyCount;
}
