package com.jspiders.ombs.exception;

public class CannotUseSamePasswordException extends RuntimeException {

	private String message;

	public CannotUseSamePasswordException(String message) {
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
