package com.jspiders.ombs.util.exception;

public class UnAuthorizedUserException extends RuntimeException{
	
	private String message;
	
	public UnAuthorizedUserException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
