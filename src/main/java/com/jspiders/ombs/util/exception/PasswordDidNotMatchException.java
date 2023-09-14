package com.jspiders.ombs.util.exception;

public class PasswordDidNotMatchException extends RuntimeException {

	private String message;
	public PasswordDidNotMatchException(String message)
	{
		this.message = message;
	}
	public String getMessage() {
 return message;
	}
}
