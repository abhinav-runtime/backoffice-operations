package com.backoffice.operations.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name = "az_contact_us_bk")
public class ContactUSEntity {

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;
	
    private String contactType;

    private String contactNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

}
