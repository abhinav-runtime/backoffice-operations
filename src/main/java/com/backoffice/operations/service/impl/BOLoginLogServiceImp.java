package com.backoffice.operations.service.impl;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.BOLoginLog;
import com.backoffice.operations.repository.BOLoginLogRepo;
import com.backoffice.operations.service.BOLoginLogSevice;

@Service
public class BOLoginLogServiceImp implements BOLoginLogSevice {

	@Autowired
	private BOLoginLogRepo boLoginLogRepo;
	
	@Override
	public void saveLoginLog(String accessToken, String accessMedia) {
		Date date = new Date(System.currentTimeMillis());
		BOLoginLog boLoginLog = new BOLoginLog();
		boLoginLog.setAccessToken(accessToken);
		boLoginLog.setAccessMedia(accessMedia);
		boLoginLog.setLoginTime(date);
		boLoginLogRepo.save(boLoginLog);
	}

}
