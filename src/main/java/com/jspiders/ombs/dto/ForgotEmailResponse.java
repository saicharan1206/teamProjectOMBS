package com.jspiders.ombs.dto;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class ForgotEmailResponse {
	private int userId;
	private String userEmail;
	private LocalDateTime createdDateTime;
	private String createdBy;
}
