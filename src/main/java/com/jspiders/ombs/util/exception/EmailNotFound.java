package com.jspiders.ombs.util.exception;

public class EmailNotFound extends RuntimeException {
	private String message;
	public EmailNotFound(String message)
	{
		this.message = message;
	}
	public String getMessage() {
 return message;
	}
}
