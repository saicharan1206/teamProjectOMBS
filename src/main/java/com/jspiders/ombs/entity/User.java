package com.jspiders.ombs.entity;

import org.springframework.stereotype.Component;

import com.jspiders.ombs.enums.IsDeleted;
import com.jspiders.ombs.util.Auditing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;



@Table(name="demo_user")
@Entity
@Data

public class User extends Auditing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@Column(unique = true)
	private String userEmail;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	@ManyToOne
	private UserRole role;
	IsDeleted deleted;
}
