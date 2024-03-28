package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class CardStatusDto {
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	public static class requestDto {
		String entityId;
		String kitNo;
		String flag;
		String reason;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	public static class responseDto {
		String flag;
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	public static class cardStatusDto {
		boolean isCardLocked;
	}
}
