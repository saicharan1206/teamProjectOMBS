package com.jspiders.ombs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
	@NotBlank(message = "email required")
	@Email(message = "email not correct")
	private String email;
	
	@NotBlank(message = "password required")
	private String password;

}
