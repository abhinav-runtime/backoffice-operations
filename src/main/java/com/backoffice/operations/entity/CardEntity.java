package com.backoffice.operations.entity;

import java.util.Date;
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
@Table(name = "az_card_entity_bk")
public class CardEntity {

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;
	private String uniqueKeyCivilId;
	private String civilId;
	private String name;
	private String cardNumber;
	private String expiry;
	private String status;
	private Integer cardType;
	private String cardKitNo;
	private String dobOfUser;
	private Date cardIssueDate;
	private String pinStatus;
	private String cardNetworkType;

	public String getCivilId() {
		return civilId;
	}

	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCardKitNo() {
		return cardKitNo;
	}

	public void setCardKitNo(String cardKitNo) {
		this.cardKitNo = cardKitNo;
	}

	public String getDobOfUser() {
		return dobOfUser;
	}

	public void setDobOfUser(String dobOfUser) {
		this.dobOfUser = dobOfUser;
	}

	public Date getCardIssueDate() {
		return cardIssueDate;
	}

	public void setCardIssueDate(Date cardIssueDate) {
		this.cardIssueDate = cardIssueDate;
	}

	public String getPinStatus() {
		return pinStatus;
	}

	public void setPinStatus(String pinStatus) {
		this.pinStatus = pinStatus;
	}

	public String getCardNetworkType() {
		return cardNetworkType;
	}

	public void setCardNetworkType(String cardNetworkType) {
		this.cardNetworkType = cardNetworkType;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
