package com.jspiders.ombs.dto;

import com.jspiders.ombs.entity.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDTO {
	
	@NotBlank(message = "FirstName required")
	private String userFirstName;
	
	@NotBlank(message = "LastName required")
	private String userLastName;
	
	private String userRole;
	
	//private UserRole userRole;

	@NotBlank(message = "email required")
	@Email(message = "email not correct")
	private String email;
	
	@NotBlank(message = "password required")
	private String password;

	
}
