package com.jspiders.ombs.util.exception;

public class PasswordMissMatchException  extends RuntimeException{

	private String message;

	public PasswordMissMatchException(String message) {
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
