package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CooldownAndAttemptDTO {
	int maxAttempts;
	int cooldownTime;
}
