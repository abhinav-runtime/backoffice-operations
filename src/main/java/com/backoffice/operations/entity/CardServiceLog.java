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
@Table(name = "az_card_service_log_bk")
public class CardServiceLog {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	@Column(name = "entity_id")
	private String entityId;
	@Column(name = "kit_no")
	private String kitNo;
	@Column(name = "flag")
	private String flag;
	@Column(name = "reason")
	private String reason;
	@CreationTimestamp
	@Column(name = "time_stamp")
	private Date timeStamp;
	@Column(name = "unique_key")
	private String uniqueKey;
	@Column(name = "process_status")
	private String status;
}
