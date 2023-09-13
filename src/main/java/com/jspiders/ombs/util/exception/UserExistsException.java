package com.jspiders.ombs.util.exception;

public class UserExistsException extends RuntimeException{
	private String message;

	public UserExistsException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	

}
