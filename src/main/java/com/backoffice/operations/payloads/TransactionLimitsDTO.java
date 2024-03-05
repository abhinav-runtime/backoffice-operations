package com.backoffice.operations.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLimitsDTO {

	@NotBlank(message = "Unique Key cannot be blank")
	private String uniqueKey;
	private double merchantOutletLimits;
	private double onlineShoppingLimits;
	private double atmWithdrawalLimits;
	private double tapAndPayLimits;

}
