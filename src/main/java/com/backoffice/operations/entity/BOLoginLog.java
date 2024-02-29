package com.backoffice.operations.entity;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "az_bo_login_log")
public class BOLoginLog {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, nullable = false)
	private String Id;
	@Column
	private String accessMedia;
	@Column
	private Date loginTime;
	@Column
	private Date logoutTime;
	@Column(name = "user_token")
	private String userToken;
	@Column
	private String userName;
	@Column
	private Date tokanExpireTime;
}
