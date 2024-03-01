package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
	String name;
	String mobileNumber;
	String email;
	String subCategoriesId;
}
