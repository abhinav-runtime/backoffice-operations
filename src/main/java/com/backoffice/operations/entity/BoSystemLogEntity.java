package com.backoffice.operations.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "az_bo_system_request_log_bk")
public class BoSystemLogEntity {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	@Column(name = "request_url")
	private String requestUrl;
	@Column(name = "http_method")
	private String httpMethod;
	@Column(name = "request_auth")
	private String requestBody;
	@Column(name = "response_status")
	private int responseStatus;	
	@Column(name = "timestamp", updatable = false )
	@CreationTimestamp
	private Date timestamp;
	@Column(name = "error_message")
	private String error;
}