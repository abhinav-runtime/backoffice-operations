package com.backoffice.operations.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "az_bo_product_request_bk")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoProductRequest {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "mobile_number", nullable = false)
	private String mobileNumber;
	@Column(name = "email", nullable = false)
	private String email;

	@CreationTimestamp
	@Column(name = "request_date", nullable = false)
	private Date requestDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_sub_categories_id", referencedColumnName = "id")
	private BoProductSubCategories subCategories;
}
