package com.jspiders.ombs.util.exception;

public class UserNotFoundByIdException extends RuntimeException {
	
	private String message;

	public String getMessage() {
		return message;
	}

	public UserNotFoundByIdException(String message) {
		super();
		this.message = message;
	}
	
	

}
