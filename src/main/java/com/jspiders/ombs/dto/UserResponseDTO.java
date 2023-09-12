package com.jspiders.ombs.dto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.jspiders.ombs.enums.UserRole;

import lombok.Data;
@Data
@Component
public class UserResponseDTO
{
	private int userId;
	private String userFirstName;
	private String userLastName;
	private UserRole role;
	private String userEmail;
	private LocalDateTime createdDate;
	private String createdBy="BHOJESHMANU";
	private LocalDateTime updatedDate;
	private String updatedBy;


}
