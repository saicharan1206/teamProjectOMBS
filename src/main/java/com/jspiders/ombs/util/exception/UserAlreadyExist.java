package com.jspiders.ombs.util.exception;

public class UserAlreadyExist extends RuntimeException{

	private String message;

	public UserAlreadyExist(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	
}
