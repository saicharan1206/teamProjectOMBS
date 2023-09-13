package com.jspiders.ombs.util.exception;

public class EmailAlreadyExistsException extends RuntimeException {
	private String email;
	private String message;
	public EmailAlreadyExistsException(String email, String message) {
		super();
		this.email = email;
		this.message = message;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
