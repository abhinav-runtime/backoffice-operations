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
    private boolean seenByUser;
    @NotBlank(message = "Unique Key cannot be blank")
    private String uniqueKey;

}
