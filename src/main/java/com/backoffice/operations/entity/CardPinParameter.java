package com.backoffice.operations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "az_card_pin_parameter_bk")
public class CardPinParameter {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
	private int pinLength;
	@Column(name = "cardPinMaximumAttempts")
    private int cardPinMaximumAttempts;
    private int sequentialDigits;
    private int repetitiveDigits;
    private int sessionExpiry;
    private int incorrectPinAttempts;
    @Column(name = "cardPinCooldownInSec")
    private int cardPinCooldownInSec;
    private int pinHistoryDepth;
}