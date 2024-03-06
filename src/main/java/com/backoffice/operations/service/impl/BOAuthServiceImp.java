package com.backoffice.operations.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.BOLoginLog;
import com.backoffice.operations.entity.BORole;
import com.backoffice.operations.entity.BOUser;
import com.backoffice.operations.payloads.BORegisterDTO;
import com.backoffice.operations.payloads.BOSuspendUserDTO;
import com.backoffice.operations.payloads.LoginDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.BOLoginLogRepo;
import com.backoffice.operations.repository.BORolesRepo;
import com.backoffice.operations.repository.BOUsersRepo;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BOAuthService;
import com.backoffice.operations.service.BOLoginLogSevice;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BOAuthServiceImp implements BOAuthService {
	private static final Logger logger = LoggerFactory.getLogger(BOAuthServiceImp.class);
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private final BOLoginLogSevice boLoginLogSevice;
	@Autowired
	private final BOUsersRepo boUsersRepo;
	@Autowired
	private final BORolesRepo boRolesRepo;
	@Autowired
	private final BOUserToken boUserToken;
	@Autowired
	private final BOLoginLogRepo boLoginLogRepo;

	@Override
	public GenericResponseDTO<Object> login(LoginDto loginDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {

			Optional<BOUser> bouser = boUsersRepo.findByEmail(loginDto.getUsernameOrEmail());
			BOUser user = bouser.get();
			if (user.getStatus().equals("Active")) {
				if (user != null) {
					response.setMessage("No user found with this email");
					response.setStatus("Failure");
					response.setData(null);
				}
				if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
					String userToken = boUserToken.userTokenGenerate(loginDto.getUsernameOrEmail());
					boLoginLogSevice.saveLoginLog(userToken);
					Map<String, String> accessToken = Map.of("token", userToken);
					response.setMessage("Success");
					response.setStatus("Success");
					response.setData(accessToken);
				} else {
					response.setMessage("Invalid Password");
					response.setStatus("Failure");
					response.setData(null);
				}

			} else {
				response.setMessage("User not have permission to login");
				response.setStatus("Failure");
				response.setData(null);
			}
		} catch (Exception e) {
			logger.error("Error in login  : {}", e);
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(null);
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> register(BORegisterDTO boRegisterDTO) {
		GenericResponseDTO<Object> genericResult = new GenericResponseDTO<>();
		try {
			if (boUsersRepo.existsByEmail(boRegisterDTO.getEmail())) {
				System.out.println(boUsersRepo.existsByEmail(boRegisterDTO.getEmail()));
				genericResult.setStatus("Failure");
				genericResult.setMessage("Email is already exists!.");
				genericResult.setData(new HashMap<>());
				return genericResult;
			} else {
				BOUser user = new BOUser();
				user.setFirstName(boRegisterDTO.getFirstName());
				user.setLastName(boRegisterDTO.getLastName());
				user.setUsername(boRegisterDTO.getUsername());
				user.setEmail(boRegisterDTO.getEmail());
				user.setBranch(boRegisterDTO.getBranch());
				user.setMobile(boRegisterDTO.getMobile());
				user.setStatus("Active");
				user.setPassword(passwordEncoder.encode(boRegisterDTO.getPassword()));

				Set<BORole> roles = new HashSet<>();
				boRegisterDTO.getRoles().forEach(role -> {
					if (boRolesRepo.existsByName(role)) {
						BORole userRole = boRolesRepo.findByName(role);
						roles.add(userRole);
						user.setRoles(roles);
					}
					;
				});
				boUsersRepo.save(user);
				user.setPassword(null);
				genericResult.setData(user);
				genericResult.setStatus("Success");
				genericResult.setMessage("User registered successfully!.");
				return  genericResult;
			}
		} catch (Exception ex) {
			logger.error("Error in register  : {}", ex.getMessage());
			genericResult.setStatus("Failure");
			genericResult.setMessage("Your request cannot be processed.");
			genericResult.setData(new HashMap<>());
		}
		return genericResult;
	}

	@Override
	public GenericResponseDTO<Object> suspendUser(BOSuspendUserDTO userDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		Map<String, String> responseData = new HashMap<>();
		if (boUsersRepo.existsByEmailAndBranch(userDto.getEmail(), userDto.getBranch())) {
			BOUser user = boUsersRepo.findByEmailAndBranch(userDto.getEmail(), userDto.getBranch());
			user.setStatus("Suspend");
			user = boUsersRepo.save(user);

			responseData.put("fullName", user.getFirstName() + " " + user.getLastName());
			responseData.put("email", user.getEmail());
			responseData.put("branch", user.getBranch());
			responseData.put("mobile", user.getMobile());
			responseData.put("status", user.getStatus());

			response.setMessage("User Suspended Successfully");
			response.setStatus("Success");
			response.setData(responseData);
		} else {
			response.setMessage("User not found");
			response.setStatus("Failure");
			response.setData(responseData);
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> logout() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		Date nowDate = new Date(System.currentTimeMillis());
		if (boUserToken.isTokenExpire(boUserToken.getUserToken())) {
			try {
				BOLoginLog loginLog = boLoginLogRepo.findByUserToken(boUserToken.getUserToken());
				loginLog.setLogoutTime(nowDate);
				loginLog.setTokanExpireTime(nowDate);
				boLoginLogRepo.save(loginLog);

				response.setMessage("Logout Successfully");
				response.setStatus("Success");
				response.setData(new HashMap<>());
			} catch (Exception e) {
				response.setMessage("Something went wrong");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
			}
		} else {
			BOLoginLog loginLog = boLoginLogRepo.findByUserToken(boUserToken.getUserToken());
			loginLog.setLogoutTime(nowDate);
			boLoginLogRepo.save(loginLog);
			response.setMessage("Logout Successfully.");
			response.setStatus("Success");
			response.setData(new HashMap<>());
		}
		return response;
	}
}
