package com.jspiders.ombs.util.exception;

public class WrongPasswordException extends RuntimeException {

	private String message;

	public WrongPasswordException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
