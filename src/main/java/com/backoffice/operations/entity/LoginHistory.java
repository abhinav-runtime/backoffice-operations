package com.backoffice.operations.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "az_login_history_bk")
public class LoginHistory {
	
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    private boolean flag;
    private LocalDateTime loginTimestamp;
    private LocalDateTime logoutTimestamp;
    private String uniqueKey;
    private String lang;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public LocalDateTime getLoginTimestamp() {
		return loginTimestamp;
	}
	public void setLoginTimestamp(LocalDateTime loginTimestamp) {
		this.loginTimestamp = loginTimestamp;
	}
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public LocalDateTime getLogoutTimestamp() {
		return logoutTimestamp;
	}
	public void setLogoutTimestamp(LocalDateTime logoutTimestamp) {
		this.logoutTimestamp = logoutTimestamp;
	}
    
}
