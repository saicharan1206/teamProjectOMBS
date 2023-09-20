package com.jspiders.ombs.dto;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.jspiders.ombs.entity.UserRole;

import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Component
public class UserResponseDTO {

	private String userFirstName;
	private String userLastName;
	private String userEmail;
	private String userRole;
	
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
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
}
