package com.jspiders.ombs.dto;

import com.jspiders.ombs.entity.User_Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class UserRequestDTO {

	private String firstName;
	private String lastName;

	@Email(regexp = "[a-zA-Z0-9+_.-]+@[g][m][a][i][l]+.[c][o][m]", message = "invalid email--Should be in the extension of '@gmail.com' ")
	private String userEmail;

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "8 characters mandatory(1 upperCase,1 lowerCase,1 special Character,1 number)")
	private String userPassword;
	private User_Role user_Role;

	public User_Role getUser_Role() {
		return user_Role;
	}

	public void setUser_Role(User_Role user_Role) {
		this.user_Role = user_Role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserFirstName() {
		return firstName;
	}

	public void setUserFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getUserLastName() {
		return lastName;
	}

	public void setUserLastName(String lastName) {
		this.lastName = lastName;
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
