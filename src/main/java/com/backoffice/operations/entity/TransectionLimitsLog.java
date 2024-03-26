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

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "az_transection_limits_log_bk")
public class TransectionLimitsLog {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	@Column(name = "unique_key")
	private String uniqueKey;
	@Column(name = "request_type")
	private String requestType;
	@Column(name = "new_limits")
	private int newLimits;
	@CreationTimestamp
	@Column(name = "time_stamp")
	private Date timeStamp;
	@Column(name = "status")
	private String status;
}
