package com.jspiders.ombs.util.exception;

public class NoProductsPresentException extends RuntimeException {

	private String message;

	public NoProductsPresentException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
