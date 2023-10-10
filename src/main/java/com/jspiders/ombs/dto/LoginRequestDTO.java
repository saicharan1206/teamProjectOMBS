package com.jspiders.ombs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

	/*
	 * @NotBlank is used to not to accept blank date in email id field
	 * @Email is used to validate user entered email is valid as per email standards or not
	 */
	@NotBlank(message = "Email is required")
	@Email(message = "email should be valid")
	private String emailId;
	/*
	 * @NotBlank is used to not to accept blank date in password field
	 */
	@NotBlank(message = "Password is required")
	private String password;
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
