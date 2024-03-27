package com.backoffice.operations.payloads;

import com.backoffice.operations.entity.AnnexureTransferLimits;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnexureTransferWithSubLimitsDTO {
	private String id;
	private String subTypeLimit;
	private AnnexureTransferLimits annexureTransferLimits;
    private String globalLimit;
    private String segment;
    private long minPerTrxnAmt;
    private long maxPerTrxnAmt;
    private long dailyAmt;
    private long monthlyAmt;
    private long dailyCount;
    private long monthlyCount;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class AnnexureTransferWithSubLimitsRequestDTO {
		private String id;
		private String subTypeLimit;
		private String annexureTransferLimitsId;
		private String globalLimit;
	    private String segment;
	    private long minPerTrxnAmt;
	    private long maxPerTrxnAmt;
	    private long dailyAmt;
	    private long monthlyAmt;
	    private long dailyCount;
	    private long monthlyCount;
	}
}
