package com.jspiders.ombs.util.exception;

public class CreateNewPasswordExceptOldPassword extends RuntimeException {

	private String message;

	public CreateNewPasswordExceptOldPassword(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
