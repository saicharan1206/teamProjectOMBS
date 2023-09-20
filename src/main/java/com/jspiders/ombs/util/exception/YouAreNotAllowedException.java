package com.jspiders.ombs.util.exception;

public class YouAreNotAllowedException extends RuntimeException {

	private String message;

	public YouAreNotAllowedException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
