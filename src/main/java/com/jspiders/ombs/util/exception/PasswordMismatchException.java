package com.jspiders.ombs.util.exception;

public class PasswordMismatchException extends RuntimeException {
	private String message;

	public PasswordMismatchException(String message) {
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
