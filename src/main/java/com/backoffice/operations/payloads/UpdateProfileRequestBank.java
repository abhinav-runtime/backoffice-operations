package com.backoffice.operations.payloads;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequestBank {

    private String mobileNumber;
    private String emailAddress;
}
