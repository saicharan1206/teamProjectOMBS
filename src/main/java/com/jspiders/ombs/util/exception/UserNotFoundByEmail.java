package com.jspiders.ombs.util.exception;

public class UserNotFoundByEmail extends RuntimeException{
	private String message;
	
	public UserNotFoundByEmail(String message) {
		super();
		this.message = message;
	}

	
	public String getMessage() {
		return message;
	}


}
