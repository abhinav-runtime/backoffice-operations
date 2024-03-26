package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class TexnLimitsDisplayDto {

	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	public static class ResquestDTO {
		String ctype;
		String transactionType;
		String uniqueKey;
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	public static class ResponseDTO {
		long min_amount_per_trans;
		long max_amount_per_trans;
		AmmountDetails daily_amount;
		AmmountDetails monthly_amount;
		CountDetails daily_count;
		CountDetails monthly_count;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class AmmountDetails {
		Double total;
		Double used;
		Double available;
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class CountDetails{
		Long total;
		Long used;
		Long available;
	}
}
