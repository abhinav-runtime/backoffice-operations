package com.backoffice.operations.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.backoffice.operations.entity.BoSystemLogEntity;
import com.backoffice.operations.repository.BoLogginglogRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
	private static Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

	@Autowired
	private BoLogginglogRepository loggingRepository;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		BoSystemLogEntity log = new BoSystemLogEntity();
		try {
			log.setRequestBody(getRequestBody(request).toString());
		} catch (Exception e) {
			logger.error("Error in interceptor : {}", e.getMessage());
		}
		log.setError(ex != null ? ex.getMessage() : null);
		log.setRequestUrl(request.getRequestURI());
		log.setHttpMethod(request.getMethod());
		log.setResponseStatus(response.getStatus());
		loggingRepository.save(log);
	}

	private Object getRequestBody(HttpServletRequest request){
		
		if (request.getHeader("userToken") != null) {
			return request.getHeader("userToken");
		} else if (request.getHeader("authorization") != null) {
			return request.getHeader("authorization");
		} else {
			return null;
		}
	}
}