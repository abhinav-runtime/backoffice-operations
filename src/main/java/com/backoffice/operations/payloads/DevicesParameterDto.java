package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DevicesParameterDto {
	Boolean unsecureDevicesSupport;
	String minRequiredAndroidAppVersion;
	String minRequiredIosAppVersion;
	int maxAllowedDevices;
	String appStoreLink;
	String googlePlayStoreLink;
	int maxSimultaneousLoggedInDevices;
}