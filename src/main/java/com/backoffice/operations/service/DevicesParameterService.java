package com.backoffice.operations.service;

import com.backoffice.operations.payloads.DevicesParameterDto;

public interface DevicesParameterService {
	DevicesParameterDto getDevicesParameter();
	DevicesParameterDto updateDevicesParameter(DevicesParameterDto devicesParameterDto);
	DevicesParameterDto createDevicesParameter(DevicesParameterDto devicesParameterDto);
}
