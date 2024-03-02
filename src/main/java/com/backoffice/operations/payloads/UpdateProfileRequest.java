package com.backoffice.operations.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {

//    @Pattern(regexp = "\\d{10}", message = "Invalid mobile number")
    private String mobileNumber;
    private String civilId;
    private String expiryDate;
    @Email(message = "Invalid email address")
    private String emailAddress;
    private Boolean emailStatementFlag;
}
