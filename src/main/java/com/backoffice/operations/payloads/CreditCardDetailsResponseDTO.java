package com.backoffice.operations.payloads;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditCardDetailsResponseDTO {

    private String creditCardNumber;
    private String customerName;
    private Double outstandingBalance;
    private Double availableBalance;
}
