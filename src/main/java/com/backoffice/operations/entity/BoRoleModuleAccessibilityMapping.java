package com.backoffice.operations.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "az_bo_role_module_accessibility_mapping_bk")
public class BoRoleModuleAccessibilityMapping {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	@Column(name = "role_id")
	// @ManyToOne(targetEntity = BORole.class)
	private String roleId;
	
	@Column(name = "module_id")
	// @ManyToOne(targetEntity = BOModuleName.class)
	private String moduleId;
	
	@Column(name = "accessibility_id")
	// @ManyToOne(targetEntity = BOAccessibility.class)
	private String accessibilityId;
}
