package com.backoffice.operations.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.DevicesParameter;
import com.backoffice.operations.payloads.DevicesParameterDto;
import com.backoffice.operations.repository.DevicesParameterRepo;
import com.backoffice.operations.service.DevicesParameterService;

@Service
public class DevicesParameterServiceImp implements DevicesParameterService {
	private final static Logger logger = LoggerFactory.getLogger(DevicesParameterServiceImp.class);
	@Autowired
	private DevicesParameterRepo devicesParameterRepo;

	@Override
	public DevicesParameterDto getDevicesParameter() {
		try {
			DevicesParameter devicesParameter = devicesParameterRepo.findAll().get(0);
			return DevicesParameterDto.builder().unsecureDevicesSupport(devicesParameter.isUnsecureDevicesSupport())
					.minRequiredAndroidAppVersion(devicesParameter.getMinRequiredAndroidAppVersion())
					.minRequiredIosAppVersion(devicesParameter.getMinRequiredIosAppVersion())
					.maxAllowedDevices(devicesParameter.getMaxAllowedDevices())
					.appStoreLink(devicesParameter.getAppStoreLink())
					.googlePlayStoreLink(devicesParameter.getGooglePlayStoreLink())
					.maxSimultaneousLoggedInDevices(devicesParameter.getMaxSimultaneousLoggedInDevices()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public DevicesParameterDto updateDevicesParameter(DevicesParameterDto devicesParameterDto) {
		try {
			DevicesParameter devicesParameter = devicesParameterRepo.findAll().get(0);
			if (devicesParameterDto.isUnsecureDevicesSupport()) {
				devicesParameter.setUnsecureDevicesSupport(devicesParameterDto.isUnsecureDevicesSupport());
			}
			if (devicesParameterDto.getMinRequiredAndroidAppVersion() != null) {
				devicesParameter.setMinRequiredAndroidAppVersion(devicesParameterDto.getMinRequiredAndroidAppVersion());
			}
			if (devicesParameterDto.getMinRequiredIosAppVersion() != null) {
				devicesParameter.setMinRequiredIosAppVersion(devicesParameterDto.getMinRequiredIosAppVersion());
			}
			if (devicesParameterDto.getMaxAllowedDevices() != 0) {
				devicesParameter.setMaxAllowedDevices(devicesParameterDto.getMaxAllowedDevices());
			}
			if (devicesParameterDto.getAppStoreLink() != null) {
				devicesParameter.setAppStoreLink(devicesParameterDto.getAppStoreLink());
			}
			if (devicesParameterDto.getGooglePlayStoreLink() != null) {
				devicesParameter.setGooglePlayStoreLink(devicesParameterDto.getGooglePlayStoreLink());
			}
			if (devicesParameterDto.getMaxSimultaneousLoggedInDevices() != 0) {
				devicesParameter
						.setMaxSimultaneousLoggedInDevices(devicesParameterDto.getMaxSimultaneousLoggedInDevices());
			}
			devicesParameter = devicesParameterRepo.save(devicesParameter);
			return DevicesParameterDto.builder().unsecureDevicesSupport(devicesParameter.isUnsecureDevicesSupport())
					.minRequiredAndroidAppVersion(devicesParameter.getMinRequiredAndroidAppVersion())
					.minRequiredIosAppVersion(devicesParameter.getMinRequiredIosAppVersion())
					.maxAllowedDevices(devicesParameter.getMaxAllowedDevices())
					.appStoreLink(devicesParameter.getAppStoreLink())
					.googlePlayStoreLink(devicesParameter.getGooglePlayStoreLink())
					.maxSimultaneousLoggedInDevices(devicesParameter.getMaxSimultaneousLoggedInDevices()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public DevicesParameterDto createDevicesParameter(DevicesParameterDto devicesParameterDto) {
		try {
			List<DevicesParameter> devicesParameterList = devicesParameterRepo.findAll();
			if (devicesParameterList.size() != 0) {
				return null;
			} else {
				DevicesParameter devicesParameter = DevicesParameter.builder()
						.unsecureDevicesSupport(devicesParameterDto.isUnsecureDevicesSupport())
						.minRequiredAndroidAppVersion(devicesParameterDto.getMinRequiredAndroidAppVersion())
						.minRequiredIosAppVersion(devicesParameterDto.getMinRequiredIosAppVersion())
						.maxAllowedDevices(devicesParameterDto.getMaxAllowedDevices())
						.appStoreLink(devicesParameterDto.getAppStoreLink())
						.googlePlayStoreLink(devicesParameterDto.getGooglePlayStoreLink())
						.maxSimultaneousLoggedInDevices(devicesParameterDto.getMaxSimultaneousLoggedInDevices())
						.build();
				devicesParameter = devicesParameterRepo.save(devicesParameter);
				return DevicesParameterDto.builder().unsecureDevicesSupport(devicesParameter.isUnsecureDevicesSupport())
						.minRequiredAndroidAppVersion(devicesParameter.getMinRequiredAndroidAppVersion())
						.minRequiredIosAppVersion(devicesParameter.getMinRequiredIosAppVersion())
						.maxAllowedDevices(devicesParameter.getMaxAllowedDevices())
						.appStoreLink(devicesParameter.getAppStoreLink())
						.googlePlayStoreLink(devicesParameter.getGooglePlayStoreLink())
						.maxSimultaneousLoggedInDevices(devicesParameter.getMaxSimultaneousLoggedInDevices()).build();
			}
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public DevicesParameterDto deleteDevicesParameter() {
		try {
			DevicesParameter devicesParameter = devicesParameterRepo.findAll().get(0);
			devicesParameterRepo.delete(devicesParameter);
			return DevicesParameterDto.builder().unsecureDevicesSupport(devicesParameter.isUnsecureDevicesSupport())
					.minRequiredAndroidAppVersion(devicesParameter.getMinRequiredAndroidAppVersion())
					.minRequiredIosAppVersion(devicesParameter.getMinRequiredIosAppVersion())
					.maxAllowedDevices(devicesParameter.getMaxAllowedDevices())
					.appStoreLink(devicesParameter.getAppStoreLink())
					.googlePlayStoreLink(devicesParameter.getGooglePlayStoreLink())
					.maxSimultaneousLoggedInDevices(devicesParameter.getMaxSimultaneousLoggedInDevices()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}

	}

}
