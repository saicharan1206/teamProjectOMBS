package com.jspiders.ombs.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
	
	private Integer userId;
	private String userFirstName;
	private String userLastName;
	private String email;
	private String userRole;
}
