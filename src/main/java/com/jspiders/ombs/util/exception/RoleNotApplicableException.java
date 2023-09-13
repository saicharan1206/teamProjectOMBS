package com.jspiders.ombs.util.exception;

public class RoleNotApplicableException extends RuntimeException{
	private String message;
	
	public RoleNotApplicableException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}
