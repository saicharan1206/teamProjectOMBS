package com.jspiders.ombs.util.exception;

import lombok.Data;

@Data
public class ProductNotFoundByIdException extends RuntimeException {
	String message;

	public ProductNotFoundByIdException(String message) {
		super();
		this.message = message;
	}
}
