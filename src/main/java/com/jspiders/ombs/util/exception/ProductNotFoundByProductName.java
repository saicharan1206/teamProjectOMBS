package com.jspiders.ombs.util.exception;

public class ProductNotFoundByProductName extends RuntimeException {
	
private String message;
	
	public ProductNotFoundByProductName(String message) {
		this.message=message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
