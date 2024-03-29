package com.backoffice.operations.payloads;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BORegisterDTO {
	
	 String firstName;
	 String lastName;
	 String mobile;
	 String branch;
	 String username;
	 String email;
	 String password;
	 Set<String> roles;
}
