package com.backoffice.operations.payloads;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreditCardTrasactionChangeDto {
	String entityId;
	Boolean international;
	Boolean dcc;
	String currency;
	Boolean isLimitUpgrade;
	Boolean isOverLimitAllowed;
	String preferredLanguage;
	String transactionUsageType;
	Boolean atm;
	Boolean ecom;
	Boolean pos;
	Boolean contactless;
	JsonNode disallowedRuleConfig;
	List<LimitConfigs> limitConfigs;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	public static class LimitConfigs {
		String channel;
		String txnType;
		double dailyLimitValue;
		double dailyLimitCnt;
		double monthlyLimitValue;
		double monthlyLimitCnt;
		double yearlyLimitValue;
		double yearlyLimitCnt;
		double minAmount;
		double maxAmount;
		String currency;
		String category;
	}
}
