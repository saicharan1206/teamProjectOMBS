package com.jspiders.ombs.util.exception;

import lombok.Data;

@Data
public class emailIdAlreadyPresentException extends RuntimeException {
	private String message;

	public emailIdAlreadyPresentException(String message) {
		super();
		this.message = message;
	}
}
