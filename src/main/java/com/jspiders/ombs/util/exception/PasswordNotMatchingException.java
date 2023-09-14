package com.jspiders.ombs.util.exception;

public class PasswordNotMatchingException  extends RuntimeException{
	private String message;
	public PasswordNotMatchingException(String message) {
		this.message=message;
	}
@Override
public String getMessage() {
	return message;
	
}
}
