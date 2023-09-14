package com.jspiders.ombs.dto;

import com.jspiders.ombs.entity.User_Role;

public class UserResponseDTO {

	private int userId;
	private String userEmail;
	private String firstName;
	private String lastName;
	private User_Role userRloe;
		
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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
	public User_Role getUserRloe() {
		return userRloe;
	}
	public void setUserRloe(User_Role userRloe) {
		this.userRloe = userRloe;
	}

	
	
}
