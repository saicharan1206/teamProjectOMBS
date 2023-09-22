package com.jspiders.ombs.util.exception;

public class InvalidUserException extends RuntimeException {

	private String message;

	public InvalidUserException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	
}
