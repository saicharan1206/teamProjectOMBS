package com.jspiders.ombs.dto;

/*
 * This is to give back response to user ,in which we are showing userId
 *  and emailId only to user 
 */
public class UserResponseDTO {

	private int userId;
	private String userFirstName;
	private String userLastName;
	private String emailId;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
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
	
	
	
}
