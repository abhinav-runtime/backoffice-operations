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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "az_bo_product_sub_categories_bk")
public class BoProductSubCategories {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Column(nullable = false, unique = true)
	private String subCategoriesName;
	@Column(nullable = false)
	private Date issueDate;
	@Column(nullable = true)
	private Date expireDate;
	@Column(nullable = false)
	private String productTitle;
	@Column(nullable = false, length = 1000)
	private String description;
	@Column(nullable = false, length = 1000)
	private String features;
	@Column(nullable = false, length = 1000)
	private String benefits;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_categories_id", referencedColumnName = "id")
	private BoProductCategories categories;
}
