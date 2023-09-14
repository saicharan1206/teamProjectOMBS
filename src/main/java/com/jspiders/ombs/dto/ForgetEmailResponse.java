package com.jspiders.ombs.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ForgetEmailResponse {
	private int userId;
	
	private String userEmail;
	private LocalDateTime createDateTime;
	private String createBy;

}
