package com.jspiders.ombs.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jspiders.ombs.enums.IsDeleted;
import com.jspiders.ombs.util.Auditing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "demo_user")
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditing{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@Column(unique = true)
	private String userEmail;
	private String password;
	private String userFirstName;
	private String userLastName;
	
	@ManyToOne
	private UserRole role;
	
	IsDeleted isDeleted;

}
