package com.jspiders.ombs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRequestDTO {

	@NotNull(message = "First name field should not be empty")
	private String userFirstName;
	
	@NotNull(message = "Last name field should not be empty")
	private String userLastName;
	
	@NotNull(message = "Role field should not be empty")
	private String userRole;
	
	@NotBlank(message = "User Email field Should not be BLANK")
	@Email(regexp = "[a-zA-Z0-9+_.-]+@[g][m][a][i][l]+.[c][o][m]", 
			message = "invalid email--Should be in the extension of '@gmail.com' ")
	private String userEmail;
	
	private String userPassword;
	
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
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}	
}

