package com.jspiders.ombs.dto;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class UserResponseDTO {
	private int userId;
	private String userEmail;
	private LocalDateTime createdDateTime;
	private String createdBy;
	private LocalDateTime updatedDateTime;
	private String updatedBy;
	private String userFirstName;
	private String userLastName;
	private String userRole;
}
