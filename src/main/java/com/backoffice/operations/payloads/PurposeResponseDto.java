package com.backoffice.operations.payloads;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PurposeResponseDto {

    @JsonProperty("category")
    private String category;
    @JsonProperty("purposeList")
    private List<purposeListDTO> purposeList;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class purposeListDTO {    	
        @JsonProperty("purpose")
        private String purpose;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
	public static class categoryDTO {
		@JsonProperty("category")
		private String category;
	}
}