package com.jspiders.ombs.util.exception;

public class UserAlreadyExistException extends RuntimeException
{
	private String message;

	public UserAlreadyExistException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}


}
