package com.jspiders.ombs.util.exception;

public class ProductAlreadyExistException extends RuntimeException {
	
	private String message;
	public ProductAlreadyExistException(String message){
		this.message=message;
	}
	@Override
	public String getMessage() {
		return message;
	}

}
