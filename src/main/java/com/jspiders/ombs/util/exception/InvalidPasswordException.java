package com.jspiders.ombs.util.exception;

public class InvalidPasswordException extends RuntimeException {

	private String message;

	public InvalidPasswordException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	
}
