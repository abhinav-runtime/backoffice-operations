package com.backoffice.operations.payloads;

import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmsControlParameterDTO {

    private String id;
    private String variable;
    private String value;

    // Add any additional fields or methods if needed
}
































































