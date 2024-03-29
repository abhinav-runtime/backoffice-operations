package com.backoffice.operations.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.backoffice.operations.entity.BOLoginLog;
import com.backoffice.operations.entity.BORole;
import com.backoffice.operations.entity.BOUser;
import com.backoffice.operations.repository.BOLoginLogRepo;
import com.backoffice.operations.repository.BOUsersRepo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BOUserToken {

	@Autowired
	private BOUsersRepo boUsersRepo;
	
	@Autowired
	private BOLoginLogRepo boLoginLogRepo;

	public String userTokenGenerate(String username) {
		Optional<BOUser> userOptional = boUsersRepo.findByEmail(username);
		BOUser user = userOptional.get();
		return serializeUserData(user);
	}

	public String getEmailFromToken() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String token = request.getHeader("userToken");
		if (token == null || token.isEmpty()) {
			return null;
		}
		token = deserializeUserData(token);
		return token.split("\\|")[0];
	}
	
	public String getEmailFromToken(String token) {
		if (token == null || token.isEmpty()) {
			return null;
		}
		token = deserializeUserData(token);
		return token.split("\\|")[0];
	}

	public List<String> getRolesFromToken() {
	    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = attributes.getRequest();
	    String token = request.getHeader("userToken");
	    if (!isTokenExpire(token) || token == null || token.isEmpty()) {
	        return new ArrayList<>(); // Token is expired or empty, return empty list
	    }
	    token = deserializeUserData(token);
	    String rolesString = token.split("\\|")[1];
	    return Arrays.asList(rolesString.split(",")); // Assuming roles are separated by comma
	}

	public List<String> getRolesFromToken(String token) {
	    if (token == null || token.isEmpty()) {
	        return new ArrayList<>();
	    }
	    token = deserializeUserData(token);
	    String rolesString = token.split("\\|")[1];
	    return Arrays.asList(rolesString.split(",")); // Assuming roles are separated by comma
	}
	
	
	private String serializeUserData(BOUser user) {
		String randomString = UUID.randomUUID().toString();
		String tokenString = user.getEmail() + "|"
				+ user.getRoles().stream().map(BORole::getName).collect(Collectors.joining(",")) + "|" + randomString;

		return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenString.getBytes());
	}

	private String deserializeUserData(String token) {
		return new String(Base64.getUrlDecoder().decode(token));
	}
	
	public Boolean isTokenExpire(String token) {
		
		BOLoginLog loginLog = boLoginLogRepo.findByUserToken(token);
		if (loginLog == null) {
			return false;
		}
		Date tokenExpiry = loginLog.getTokanExpireTime();
		return tokenExpiry != null && tokenExpiry.after(new Date()); // Return true if token is not expired
	}
	
	public String getUserToken() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		return request.getHeader("userToken");
	}
}
