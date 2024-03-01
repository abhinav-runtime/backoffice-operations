package com.backoffice.operations.payloads;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoProductCategoriesRequestDTO {
	String categoriesName;
	String subCategoriesName;
	Date expireDate;
	String imgUrl;
}
