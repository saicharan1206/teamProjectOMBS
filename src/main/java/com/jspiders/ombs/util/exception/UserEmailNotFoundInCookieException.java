package com.jspiders.ombs.util.exception;

public class UserEmailNotFoundInCookieException extends RuntimeException {

	private String message;

	public UserEmailNotFoundInCookieException(String message) {
		this.message=message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
