package com.jspiders.ombs.dto;

import java.util.Date;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/*
 * This is to take user Request from UI,which is accepting emailid ,password only
 */
public class UserRequestDTO {

	@NotBlank(message = "UserFirstName is required")
	private String userFirstName;
	@NotBlank(message = "UserLastName is required")
	private String userLastName;
	@NotBlank(message = "UserRole is required")
	private String userRole;
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
	
	
	
}
