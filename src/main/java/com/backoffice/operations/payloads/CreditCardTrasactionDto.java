package com.backoffice.operations.payloads;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardTrasactionDto {
	
	private List<LimitConfig> limitConfig;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class LimitConfig{
		private String channelType;
		private String maxAmount;
	}
}
