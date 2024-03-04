package com.backoffice.operations.payloads;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoCategoriesAndSubCategoriesUpdateDTO {
	@NotNull(message = "id is required")
	String id;
	String categoriesName;
	String subCategoriesName;
	String productTitle;
	Date issueDate;
	Date expireDate;
	String benefits;
	String description;
	String features;
}
