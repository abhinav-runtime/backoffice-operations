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
	
	@NotBlank(message = "Customer I'd cannot be blank")
	private String customerId;
	private double merchantOutletLimits;
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OnlineShopping {
		@NotBlank(message = "Customer I'd cannot be blank")
		private String customerId;
		private double onlineShoppingLimits;
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ATMwithdrawal {
		@NotBlank(message = "Customer I'd cannot be blank")
		private String customerId;
		private double atmWithdrawalLimits;	
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TapAndPay {
		@NotBlank(message = "Customer I'd cannot be blank")
		private String customerId;
		private double tapAndPayLimits;	
	}	
}
