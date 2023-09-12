package com.jspiders.ombs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRequestDTO {
	
	@NotBlank(message = "FirstName required")
	private String userFirstName;
	
	@NotBlank(message = "LastName required")
	private String userLastName;
	
	@NotBlank(message = "Role required")
	private String userRole;
	
	@NotBlank(message = "email required")
	@Email(message = "email not correct")
	private String email;
	
	@NotBlank(message = "password required")
	private String password;

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
