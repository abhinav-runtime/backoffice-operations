package com.backoffice.operations.payloads;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BOCustomerDetailsResponseDTO {

	public String cusId;
	public String status;
	public String uname;
	public String name;
	public String dob;
	public String nationalType;
	public String nationalId;
	public String nationalExpiry;
	public String nationality;
	public String motherName;
	public String gender;
	public String maritalStatus;
	public String profession;
	public String dateRegister;
	public String branch;
	public String roles;
	public JsonNode segment;
	public String staff;
	public String estatment;
	public String accesptedvalueDisclamer;
	public String registrantionStatus;
	public String phone;
	public String mobile;
	public String email;
	public String fax;
	public String address1;
	public String address2;
	public String address3;
	public String country;
}
