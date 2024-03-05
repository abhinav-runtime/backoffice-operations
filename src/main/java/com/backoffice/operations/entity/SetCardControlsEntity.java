package com.backoffice.operations.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "az_card_Controls_bk")
public class SetCardControlsEntity {
	
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;
	@Column(name= "uniqueKey")
	private String uniqueKey;
	private boolean posFlag;
	private boolean ecomFlag;
	private boolean atmFlag;
	private boolean contactlessFlag;
	private LocalDateTime dateModified;
	
}
