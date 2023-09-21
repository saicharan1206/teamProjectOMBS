package com.jspiders.ombs.dto;

import com.jspiders.ombs.entity.IsDeleted;
import com.jspiders.ombs.entity.UserRole;

public class UserRequestDTO {
	private String userEmail;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	private String userRole;
	private IsDeleted isDeleted;
	
	
	public IsDeleted getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(IsDeleted isDeleted) {
		this.isDeleted = isDeleted;
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
