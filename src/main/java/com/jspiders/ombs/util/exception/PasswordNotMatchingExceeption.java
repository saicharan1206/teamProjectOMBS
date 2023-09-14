package com.jspiders.ombs.util.exception;

public class PasswordNotMatchingExceeption extends RuntimeException {

	private String message;

	public PasswordNotMatchingExceeption(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}
