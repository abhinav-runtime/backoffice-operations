package com.backoffice.operations.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.repository.BORolesRepo;
import com.backoffice.operations.repository.BoAccessibilityRepo;
import com.backoffice.operations.repository.BoModuleNameRepo;
import com.backoffice.operations.repository.BoRoleMuduleAccessMappingRepo;
import com.backoffice.operations.security.BOUserToken;

@Service
public class BoAccessHelper {
	
	@Autowired
	private BoModuleNameRepo boModuleNameRepo;
	@Autowired
	private BoAccessibilityRepo boAccessibilityRepo;
	@Autowired
	private BoRoleMuduleAccessMappingRepo boRoleMuduleAccessMappingRepo;
	@Autowired
	private BORolesRepo boRolesRepo;
	@Autowired
	private BOUserToken boUserToken;
	
	public Boolean isAccessible(String moduleName, String accessType) {
	    List<String> roles = boUserToken.getRolesFromToken();
	    for (String userRole : roles) {
	        String roleId = boRolesRepo.findByName(userRole).getId();
	        String moduleId = boModuleNameRepo.findByModuleName(moduleName).getId();
	        String accessId = boAccessibilityRepo.findByAccessType(accessType).getId();
	        if (boRoleMuduleAccessMappingRepo.existsByRoleIdAndModuleIdAndAccessibilityId(roleId, moduleId, accessId)) {
	            return true;
	        }
	    }
	    return false;
	}
	
}