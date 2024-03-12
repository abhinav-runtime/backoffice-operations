package com.backoffice.operations.service;

import com.backoffice.operations.payloads.CooldownAndAttemptDTO;

public interface BoCooldownAndAttemptUpdateService {
	CooldownAndAttemptDTO updateOtpParameter(CooldownAndAttemptDTO requestDto);
	CooldownAndAttemptDTO updateCardPinParameter(CooldownAndAttemptDTO requestDto);
	CooldownAndAttemptDTO updateCivilIdParameter(CooldownAndAttemptDTO requestDto);
}
