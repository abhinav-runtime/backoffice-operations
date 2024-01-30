package com.backoffice.operations.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.payloads.CardDTO;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.service.CivilIdService;

@Service
public class CivilIdServiceImpl implements CivilIdService {
	
	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
    public String validateCivilId(String civilId) {
        return civilId.matches("\\d{8}") ? "Success" : "Failure";
    }

	@Override
    public List<CardDTO> getCardListing(String civilId) {
        List<CardEntity> cardEntities = cardRepository.findByCivilId(civilId);
        return cardEntities.stream()
                .map(cardEntity -> modelMapper.map(cardEntity, CardDTO.class))
                .collect(Collectors.toList());
    }
	
}
