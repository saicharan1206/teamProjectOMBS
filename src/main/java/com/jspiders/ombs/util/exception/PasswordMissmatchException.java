package com.jspiders.ombs.util.exception;

public class PasswordMissmatchException extends RuntimeException {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PasswordMissmatchException(String message) {
		super();
		this.message = message;
	}

}
