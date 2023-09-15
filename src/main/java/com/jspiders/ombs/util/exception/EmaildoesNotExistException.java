package com.jspiders.ombs.util.exception;

public class EmaildoesNotExistException  extends RuntimeException{
	private String message;

	public String getMessage() {
		return message;
	}


	public EmaildoesNotExistException(String message) {
		super();
		this.message = message;
	}
	
	
	
}
