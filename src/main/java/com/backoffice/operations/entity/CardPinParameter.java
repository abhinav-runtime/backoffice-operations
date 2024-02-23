package com.backoffice.operations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "az_card_pin_parameter_bk")
public class CardPinParameter {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
	
	@Column(name = "cardPinMaximumAttempts")
    private int cardPinMaximumAttempts;
    
    @Column(name = "cardPinCooldownInSec")
    private int cardPinCooldownInSec;

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCardPinMaximumAttempts() {
		return cardPinMaximumAttempts;
	}

	public void setCardPinMaximumAttempts(int cardPinMaximumAttempts) {
		this.cardPinMaximumAttempts = cardPinMaximumAttempts;
	}

	public int getCardPinCooldownInSec() {
		return cardPinCooldownInSec;
	}

	public void setCardPinCooldownInSec(int cardPinCooldownInSec) {
		this.cardPinCooldownInSec = cardPinCooldownInSec;
	}
    
}
