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
@Table(name = "az_card_setting_log_bk")
public class CardSettingLog {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	@Column(name = "atm")
	private Boolean atm;
	@Column(name = "pos")
	private Boolean pos;
	@Column(name = "ecom")
	private Boolean ecom;
	@Column(name = "contactless")
	private Boolean contactless;
	@CreationTimestamp
	@Column(name = "time_stamp")
	private Date timeStamp;
	@Column(name = "unique_key")
	private String uniqueKey;
	@Column(name = "staus")
	private String status;
}
