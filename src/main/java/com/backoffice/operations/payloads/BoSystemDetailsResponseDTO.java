package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoSystemDetailsResponseDTO {
	 String deviceId;
	 String name;
	 String type;
	 String model;
	 String status;
	 String appVersion;
	 String carrier;
	 String location;
	 String iPAddress;
}