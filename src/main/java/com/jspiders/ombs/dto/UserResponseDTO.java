package com.jspiders.ombs.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponseDTO {

	private int userId;
	private LocalDateTime createdDate;
	private String createdBy="yogesh";
	private LocalDateTime updatedDate;
	private String updatedBy;
}
