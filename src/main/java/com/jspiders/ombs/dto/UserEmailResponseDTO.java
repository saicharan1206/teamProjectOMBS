package com.jspiders.ombs.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserEmailResponseDTO 
{
	private int userId;
	private String userEmail;
	

}
