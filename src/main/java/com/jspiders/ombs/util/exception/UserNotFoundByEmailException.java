package com.jspiders.ombs.util.exception;

public class UserNotFoundByEmailException extends RuntimeException {

	private String message;

	public UserNotFoundByEmailException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
