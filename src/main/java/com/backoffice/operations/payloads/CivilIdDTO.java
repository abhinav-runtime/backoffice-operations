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
public class CivilIdDTO {

	@NotBlank(message = "Civil ID cannot be blank")
    @Pattern(regexp = "\\d{8}", message = "Civil ID must be 8 digits")
    private String civilId;

	public String getCivilId() {
		return civilId;
	}

	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	
}
