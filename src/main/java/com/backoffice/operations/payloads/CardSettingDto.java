package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CardSettingDto {
	Boolean atm;
	Boolean pos;
	Boolean ecom;
	Boolean contactless;
}
