package com.backoffice.operations.payloads;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {

    private String mobileNumber;
    private String emailAddress;
    private Boolean emailStatementFlag;
}
