package com.backoffice.operations.service.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.BoRoleModuleAccessibilityMapping;
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
	
	public Set<Map<String, String>> accessAssignResponse(List<BoRoleModuleAccessibilityMapping> accessAssignList) {
		Set<Map<String, String>> accessAssignSet = new HashSet<>();
		accessAssignList.forEach(access -> {
			Map<String, String> tempMap = new LinkedHashMap<>();
			tempMap.put("role", boRolesRepo.findById(access.getRoleId()).get().getName());
			tempMap.put("module", boModuleNameRepo.findById(access.getModuleId()).get().getModuleName());
			tempMap.put("accessType",
					boAccessibilityRepo.findById(access.getAccessibilityId()).get().getAccessType());
			accessAssignSet.add(tempMap);
		});
		return accessAssignSet;
	}
}
