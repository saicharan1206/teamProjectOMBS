package com.jspiders.ombs.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jspiders.ombs.enums.IsDeleted;
import com.jspiders.ombs.util.Auditing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;



@Table(name="demo_user")
@Entity
@Data

public class User extends Auditing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@Column(unique = true)
	@Email(regexp = "[a-zA-Z0-9+_.-]+@[g][m][a][i][l]+.[c][o][m]", message = "invalid email--Should be in the extension of '@gmail.com' ")
	private String userEmail;
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "8 characters mandatory(1 upperCase,1 lowerCase,1 special Character,1 number)")
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	@ManyToOne
	private UserRole role;
	IsDeleted deleted;
	@OneToMany(mappedBy = "user")
	private List<Product> products;
}
