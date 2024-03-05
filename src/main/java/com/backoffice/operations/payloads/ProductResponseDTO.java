package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
	String name;
	String mobileNumber;
	String email;
	String subCategories;
	String categories;
	String requestDate;
	String referenceId;
	String id;
}
