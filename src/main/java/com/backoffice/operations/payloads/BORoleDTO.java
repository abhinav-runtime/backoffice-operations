package com.backoffice.operations.payloads;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BORoleDTO {
	String name;
	long priority;
	Set<BOModuleDTO> modules;
}
