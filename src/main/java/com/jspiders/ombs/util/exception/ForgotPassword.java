package com.jspiders.ombs.util.exception;

public class ForgotPassword extends RuntimeException {
	private String message;

	public ForgotPassword(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
