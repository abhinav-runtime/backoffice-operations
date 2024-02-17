package com.backoffice.operations.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityIdDTO {

	@NotBlank(message = "First Four Digits Card No cannot be blank")
	@Pattern(regexp = "\\d{4}", message = "First Four Digits Card No must be 4 digits")
	private String firstFourDigitscardNo;

	@NotBlank(message = "Last Four Digits Card No cannot be blank")
	@Pattern(regexp = "\\d{4}", message = "Last Four Digits Card No must be 4 digits")
	private String lastFourDigitscardNo;

	@NotBlank(message = "Unique Key cannot be blank")
	private String uniqueKey;
	
	private String lang;

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getFirstFourDigitscardNo() {
		return firstFourDigitscardNo;
	}

	public void setFirstFourDigitscardNo(String firstFourDigitscardNo) {
		this.firstFourDigitscardNo = firstFourDigitscardNo;
	}

	public String getLastFourDigitscardNo() {
		return lastFourDigitscardNo;
	}

	public void setLastFourDigitscardNo(String lastFourDigitscardNo) {
		this.lastFourDigitscardNo = lastFourDigitscardNo;
	}

}
