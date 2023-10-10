package com.jspiders.ombs.util.exception;

import lombok.Data;

@Data
public class ProductAlreadyPresentException extends RuntimeException {
	private String message;

	public ProductAlreadyPresentException(String message) {
		super();
		this.message = message;
	}
}
