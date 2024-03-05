package com.backoffice.operations.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {

    private String id;
    private String imageUrl;
    private String uniqueKey;
    private String text1;
    private String text2;
    private int priority;
    private String urlType;
    private String url;

}
