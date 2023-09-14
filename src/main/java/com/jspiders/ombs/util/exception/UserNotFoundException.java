package com.jspiders.ombs.util.exception;

public class UserNotFoundException extends Exception {
	
	private String message;

	public UserNotFoundException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
