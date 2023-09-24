package com.jspiders.ombs.util.exception;

public class ProductNotFoundByIdException extends RuntimeException {

	private String message;

	public ProductNotFoundByIdException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
