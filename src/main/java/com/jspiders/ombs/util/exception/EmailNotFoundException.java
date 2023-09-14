package com.jspiders.ombs.util.exception;

public class EmailNotFoundException extends RuntimeException{
	private String message;

	public EmailNotFoundException(String message) {
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
