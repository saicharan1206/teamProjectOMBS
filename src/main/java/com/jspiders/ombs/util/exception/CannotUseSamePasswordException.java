package com.jspiders.ombs.util.exception;

import lombok.Data;

@Data
public class CannotUseSamePasswordException extends RuntimeException {
	private String message;

	public CannotUseSamePasswordException(String message) {
		super();
		this.message = message;
	}
}
