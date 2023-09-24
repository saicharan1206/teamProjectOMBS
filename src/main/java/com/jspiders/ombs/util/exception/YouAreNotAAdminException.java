package com.jspiders.ombs.util.exception;

public class YouAreNotAAdminException extends RuntimeException {
	
	private String message;
	public YouAreNotAAdminException(String message) {
		this.message=message;
	}
	
	public String getMessage() {
		return message;
	}
	

}
