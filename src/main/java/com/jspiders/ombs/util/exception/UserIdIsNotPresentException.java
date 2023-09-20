package com.jspiders.ombs.util.exception;

public class UserIdIsNotPresentException extends RuntimeException {
	
	private String message;

	public UserIdIsNotPresentException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
