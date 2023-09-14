package com.jspiders.ombs.entity;

import java.util.ArrayList;
import java.util.List;

import com.jspiders.ombs.util.Auditing;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class UserRole extends Auditing{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String role;
	@OneToMany (mappedBy = "role")
	private List<User> user=new ArrayList<>();
}
