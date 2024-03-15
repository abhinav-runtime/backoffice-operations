package com.backoffice.operations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "az_devices_parameter_bk")
public class DevicesParameter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@Column(name = "unsecure_devices_support", nullable = false)
	private Boolean unsecureDevicesSupport;
	@Column(name = "min_required_android_app_version", nullable = false)
	private String minRequiredAndroidAppVersion;
	@Column(name = "min_required_ios_app_version", nullable = false)
	private String minRequiredIosAppVersion;
	@Column(name = "max_allowed_devices", nullable = false)
	private int maxAllowedDevices;
	@Column(name = "app_store_link", nullable = true)
	private String appStoreLink;
	@Column(name = "google_play_store_link", nullable = true)
	private String googlePlayStoreLink;
	@Column(name = "max_simultaneous_logged_in_devices", nullable = false)
	private int maxSimultaneousLoggedInDevices;
}