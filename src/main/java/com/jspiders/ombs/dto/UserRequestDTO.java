package com.jspiders.ombs.dto;

import lombok.Data;

@Data
public class UserRequestDTO {

	private String userEmail;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	private String role;
}
