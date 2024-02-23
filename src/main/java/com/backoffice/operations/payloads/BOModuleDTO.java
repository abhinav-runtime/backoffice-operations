package com.backoffice.operations.payloads;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BOModuleDTO {
	String moduleName;
	Set<String> accessTypes;
}
