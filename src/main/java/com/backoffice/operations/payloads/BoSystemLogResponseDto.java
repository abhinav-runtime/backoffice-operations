package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoSystemLogResponseDto {

	String id;
	String requestUrl;
	String httpMethod;
	String authType;
	int responseStatus;
	String error;
	String timestamp;
}
