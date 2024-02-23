package com.backoffice.operations.service.impl;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.backoffice.operations.entity.BOLoginLog;
import com.backoffice.operations.repository.BOLoginLogRepo;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BOLoginLogSevice;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BOLoginLogServiceImp implements BOLoginLogSevice {

	long tenHoursInMillis = 5 * 60 * 60 * 1000; // 5 hours in milliseconds
	
	@Autowired
	private BOLoginLogRepo boLoginLogRepo;
	
	@Autowired
	private BOUserToken boUserToken;
	
	@Override
	public void saveLoginLog(String userToken) {
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String accessMedia = request.getHeader("User-Agent");
		
		String username = boUserToken.getEmailFromToken(userToken);
		
		Date date = new Date(System.currentTimeMillis());
		
		Date tokenExpiry = new Date(System.currentTimeMillis() + tenHoursInMillis);
		
		BOLoginLog boLoginLog = new BOLoginLog();
		boLoginLog.setAccessMedia(accessMedia);
		boLoginLog.setLoginTime(date);
		boLoginLog.setUserName(username);
		boLoginLog.setUserToken(userToken);
		boLoginLog.setTokanExpireTime(tokenExpiry);
		boLoginLogRepo.save(boLoginLog);
	}

}
