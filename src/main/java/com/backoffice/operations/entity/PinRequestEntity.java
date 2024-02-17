package com.backoffice.operations.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "az_pin_requests_bk")
public class PinRequestEntity {

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;
	private String uniqueKeyCivilId;
	private String civilId;
	private String pin;
	private String kitNo;
	private String expiryDate;
	private String dob;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUniqueKeyCivilId() {
		return uniqueKeyCivilId;
	}

	public void setUniqueKeyCivilId(String uniqueKeyCivilId) {
		this.uniqueKeyCivilId = uniqueKeyCivilId;
	}

	public String getCivilId() {
		return civilId;
	}

	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getKitNo() {
		return kitNo;
	}

	public void setKitNo(String kitNo) {
		this.kitNo = kitNo;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

}
