package com.backoffice.operations.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CivilIdParameterDTO {
    @NotNull(message = "Max attempts cannot be null")
	int maxAttempts;
	@NotNull(message = "Cooldown time cannot be null")
	int cooldownTime;
	@NotNull(message = "Overrall max attempts cannot be null")
	int overrallMaxAttempts;
	@NotNull(message = "Device attempts cannot be null")
	int deviceAttempts;
}
