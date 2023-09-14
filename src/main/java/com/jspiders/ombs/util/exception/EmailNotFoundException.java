package com.jspiders.ombs.util.exception;

public class EmailNotFoundException extends RuntimeException {
	
	private String message;

	public EmailNotFoundException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	

}
