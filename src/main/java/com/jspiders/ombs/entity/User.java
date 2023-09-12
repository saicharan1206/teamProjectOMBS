package com.jspiders.ombs.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jspiders.ombs.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="demo_user")
public class User extends Auditable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@Column(name = "First_Name")
	private String userFirstName;
	@Column(name = "Last_Name")
	private String userLastName;
	private UserRole role;
	@Column(unique = true)
	private String userEmail;
	@Column(unique = true)
	private String userPassword;
	

}
