package com.backoffice.operations.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "az_user_login_details_bk")
public class UserLoginDetails {
	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name = "usernameOrEmail")
	private String usernameOrEmail;
	
	@Column(name = "access", length = 280)
	private String access;
	
	@Column(name = "loginTime")
	private LocalDateTime loginTime;
	
	@Column(name = "logoutTime")
	private LocalDateTime logoutTime;

	//Getter and Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDateTime localDateTime) {
		this.loginTime = localDateTime;
	}

	public LocalDateTime getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(LocalDateTime localDateTime) {
		this.logoutTime = localDateTime;
	}
}
