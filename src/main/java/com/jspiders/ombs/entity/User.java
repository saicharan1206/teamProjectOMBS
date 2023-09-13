package com.jspiders.ombs.entity;



import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jspiders.ombs.auditfields.Audit;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="demo-user")
public class User  extends Audit<String> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String userEmail;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	

	
	@ManyToOne
	private UserRole userRole1;
	
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
	
	public UserRole getUserRole1() {
		return userRole1;
	}
	public void setUserRole1(UserRole userRole1) {
		this.userRole1 = userRole1;
	}
	

	

	
	
	
	
	
	
	
	
	

}
