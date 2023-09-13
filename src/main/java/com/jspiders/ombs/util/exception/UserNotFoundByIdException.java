package com.jspiders.ombs.util.exception;

public class UserNotFoundByIdException extends RuntimeException {
	private int Id;
	private String message;
	public UserNotFoundByIdException(int id, String message) {
		super();
		Id = id;
		this.message = message;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
