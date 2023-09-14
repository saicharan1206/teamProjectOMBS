package com.jspiders.ombs.util.exception;

public class EmailDoesnotExistsException extends RuntimeException {
	private String message;

	public EmailDoesnotExistsException(String message) {
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
