package com.jspiders.ombs.util.exception;

public class EmailDoesNotExistException extends RuntimeException {

	private String message;

	public EmailDoesNotExistException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	
}
