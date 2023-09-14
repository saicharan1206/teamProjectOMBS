package com.jspiders.ombs.entity;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jspiders.ombs.util.Auditing;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_role")
public class UserRole extends Auditing {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String userRole;
	@OneToMany(mappedBy = "role")
	private List<User> user;
	
}