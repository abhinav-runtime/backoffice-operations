package com.backoffice.operations.service;

import java.util.List;

import com.backoffice.operations.payloads.CardDTO;

public interface CivilIdService {
	
	String validateCivilId(String civilId);
	
	List<CardDTO> getCardListing(String civilId);

}
