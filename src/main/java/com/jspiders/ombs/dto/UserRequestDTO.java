package com.jspiders.ombs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRequestDTO {
	@NotBlank(message="Email cannot be Blank")
	@Email(regexp="[a-zA-Z0-9+_.-]+@[g][m][a][i][l]+.[c][o][m]",
	message="invalid email--should be in extension of '@gmail.com' ")
	private String email;
	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8}.$",
			message="8 characters mandatory(1 upperCase,1 lowerCase,1 special character,1 number)")
    private String password;
	private String firstname;
	private String lastname;
	private String userRole;

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
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;

	}
}
