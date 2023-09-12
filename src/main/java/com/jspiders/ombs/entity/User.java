package com.jspiders.ombs.entity;

/***
 1.This is an Entity class in which private variables and 
 	their respective getters and setters are created
 2. The class 'User' is annotated with 
 	@Entity - this annotation indicates the JPA entity and creates a table in the database with the 
 				same name as class
 				
 	@EntityListeners(AuditingEntityListener.class) - 
 	Specifies that the AuditingEntityListener class should be used as an 
 	entity listener for this entity.
 	
 	@Id - Designates the id field as the primary key for the entity.
 	
 	@GeneratedValue(strategy = GenerationType.IDENTITY) -
 	Specifies how the primary key values should be generated. 
 	In this case, GenerationType.IDENTITY indicates that the database should 
 	automatically generate the primary key values.
 	
 	@CreatedBy - it signifies that this field should automatically be 
 	populated with the information about the user or entity that created 
 	the record.
 	
 	@CreatedDate - Marks the createdDate field as the one that should be 
 	automatically populated with the timestamp when an Entity record was created.
 	
	@Temporal(TemporalType.TIMESTAMP)- Specifies that the createdDate field 
	should be stored as a timestamp in the database.
 ***/

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String userFirstName;
	private String userLastName;
	private String userRole;
	private String email;
	private String password;
	
	@CreatedBy
	private String createdBy; 
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date  createdDate;
	private Date updatedDate;
	private String updatedBy;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}
