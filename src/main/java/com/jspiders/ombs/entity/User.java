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
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String userFirstName;
	private String userLastName;
	private String email;
	private String password;
	
	@CreatedBy
	private String createdBy; 
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date  createdDate;
	private Date updatedDate;
	private String updatedBy;
	
	@ManyToOne
	private UserRole userRole;
	
	private ISDELETED deleteStatus;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Products> products;
}
