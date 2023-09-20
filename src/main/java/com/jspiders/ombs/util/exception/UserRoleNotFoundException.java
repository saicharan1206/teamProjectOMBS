package com.jspiders.ombs.util.exception;

public class UserRoleNotFoundException extends RuntimeException {

	private String message;

	public UserRoleNotFoundException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
