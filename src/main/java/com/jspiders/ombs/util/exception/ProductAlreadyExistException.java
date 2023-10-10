package com.jspiders.ombs.util.exception;

public class ProductAlreadyExistException extends RuntimeException {

	private String message;

	public ProductAlreadyExistException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
