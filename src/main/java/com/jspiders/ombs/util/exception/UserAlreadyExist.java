package com.jspiders.ombs.util.exception;

public class UserAlreadyExist extends RuntimeException {

	String message;

	public UserAlreadyExist(String message) {
		super(message);
		this.message = message;
	}
	
}
