package com.jspiders.ombs.util.exception;

public class UserRoleNotApplicableException extends RuntimeException {
private String message;
public UserRoleNotApplicableException(String message) {
	super();
	this.message = message;
}


public String getMessage() {
	return message;
}
}
