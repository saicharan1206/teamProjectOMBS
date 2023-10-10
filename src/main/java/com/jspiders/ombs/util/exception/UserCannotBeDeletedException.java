package com.jspiders.ombs.util.exception;

import lombok.Data;

@Data
public class UserCannotBeDeletedException extends RuntimeException {
	private String message;

	public UserCannotBeDeletedException(String message) {
		super();
		this.message = message;
	}

}
