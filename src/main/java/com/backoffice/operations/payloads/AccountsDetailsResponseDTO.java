package com.backoffice.operations.payloads;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountsDetailsResponseDTO {

    private String accountNumber;
    private String currency;
    private Double accountBalance;
}
