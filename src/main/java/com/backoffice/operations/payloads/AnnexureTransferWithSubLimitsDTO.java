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
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class AnnexureTransferWithSubLimitsRequestDTO {
		private String id;
		private String subTypeLimit;
		private String annexureTransferLimitsId;
	}
}
