package com.jspiders.ombs.util.exception;

public class EmailAlreadyFoundException extends RuntimeException{
	private String message;
	
	public EmailAlreadyFoundException(String message) {
		super();
		this.message = message;
	}

	
	public String getMessage() {
		return message;
	}

}
