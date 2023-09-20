package com.jspiders.ombs.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import com.jspiders.ombs.entity.IsDeleted;
import com.jspiders.ombs.entity.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
@Component
public class UserRequestDTO {

	private String userFirstName;
	private String userLastName;
	@Email(regexp = "[A-Z,a-z]{3,10}[0-9]{2,5}@gmail[.]com",message = "Email Shout be in The Form of @gmail.com")
	private String userEmail;
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "8 characters mandatory(1 upperCase,1 lowerCase,1 special Character,1 number)")
	private String userPassword;
	private String userRole;
	private IsDeleted isDeleted;
	
//	@Temporal(TemporalType.TIMESTAMP)
//	@CreatedDate
//	private LocalDateTime createdDate;
//	@CreatedBy
//	private String createdBy;
//	@Temporal(TemporalType.TIMESTAMP)
//	@LastModifiedDate
//	private LocalDateTime updatedDate;
//	@LastModifiedBy
//	private String updatedBy;
	
	public String getUserFirstName() {
		return userFirstName;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public IsDeleted getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(IsDeleted isDeleted) {
		this.isDeleted = isDeleted;
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
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
