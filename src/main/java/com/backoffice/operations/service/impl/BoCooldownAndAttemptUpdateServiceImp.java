package com.backoffice.operations.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.CardPinParameter;
import com.backoffice.operations.entity.CivilIdParameter;
import com.backoffice.operations.entity.OtpParameter;
import com.backoffice.operations.payloads.CooldownAndAttemptDTO;
import com.backoffice.operations.repository.CardPinParameterRepository;
import com.backoffice.operations.repository.CivilIdParameterRepository;
import com.backoffice.operations.repository.OtpParameterRepository;
import com.backoffice.operations.service.BoCooldownAndAttemptUpdateService;

@Service
public class BoCooldownAndAttemptUpdateServiceImp implements BoCooldownAndAttemptUpdateService {
	@Autowired
	private OtpParameterRepository otpParameterRepository;
	@Autowired
	private CardPinParameterRepository cardPinParameterRepository;
	@Autowired
	private CivilIdParameterRepository civilIdParameterRepository;

	private long id = 1;

	@Override
	public CooldownAndAttemptDTO updateOtpParameter(CooldownAndAttemptDTO requestDto) {
		OtpParameter otpParameter = otpParameterRepository.findById(id).get();
		otpParameter.setOtpMaxAttempts(requestDto.getMaxAttempts());
		otpParameter.setOtpCooldownInMin(requestDto.getCooldownTime());
		otpParameter = otpParameterRepository.save(otpParameter);
		return CooldownAndAttemptDTO.builder().maxAttempts(otpParameter.getOtpMaxAttempts())
				.cooldownTime(otpParameter.getOtpCooldownInMin()).build();
	}

	@Override
	public CooldownAndAttemptDTO updateCardPinParameter(CooldownAndAttemptDTO requestDto) {
		CardPinParameter cardPinParameter = cardPinParameterRepository.findById(id).get();
		cardPinParameter.setCardPinMaximumAttempts(requestDto.getMaxAttempts());
		cardPinParameter.setCardPinCooldownInSec(requestDto.getCooldownTime());
		cardPinParameter = cardPinParameterRepository.save(cardPinParameter);
		return CooldownAndAttemptDTO.builder().maxAttempts(cardPinParameter.getCardPinMaximumAttempts())
				.cooldownTime(cardPinParameter.getCardPinCooldownInSec()).build();
	}

	@Override
	public CooldownAndAttemptDTO updateCivilIdParameter(CooldownAndAttemptDTO requestDto) {
		CivilIdParameter civilIdParameter = civilIdParameterRepository.findById(id).get();
		civilIdParameter.setCivilIdMaxAttempts(requestDto.getMaxAttempts());
		civilIdParameter.setCivilIdCooldownInSec(requestDto.getCooldownTime());
		civilIdParameter = civilIdParameterRepository.save(civilIdParameter);
		return CooldownAndAttemptDTO.builder().maxAttempts(civilIdParameter.getCivilIdMaxAttempts())
				.cooldownTime(civilIdParameter.getCivilIdCooldownInSec()).build();
	}

}
