package com.jspiders.ombs.util.exception;

import lombok.Data;

@Data
public class EmailNotFoundException extends RuntimeException{
	private String message;

	public EmailNotFoundException(String message) {
		super();
		this.message = message;
	}
	
}
