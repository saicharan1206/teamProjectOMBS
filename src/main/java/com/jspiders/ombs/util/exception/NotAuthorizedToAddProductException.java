package com.jspiders.ombs.util.exception;

import lombok.Data;

@Data
public class NotAuthorizedToAddProductException extends RuntimeException {
	private String message;

	public NotAuthorizedToAddProductException(String message) {
		super();
		this.message = message;
	}
	
	
}
