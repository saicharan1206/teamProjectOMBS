package com.jspiders.ombs.util.exception;

public class UserAlreadyExsistException extends RuntimeException
{
	private String message;

	public UserAlreadyExsistException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	

}
