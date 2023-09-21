package com.jspiders.ombs.util.exception;

public class ProductAlreadyExistsException extends RuntimeException{

	private String message;

	public ProductAlreadyExistsException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
