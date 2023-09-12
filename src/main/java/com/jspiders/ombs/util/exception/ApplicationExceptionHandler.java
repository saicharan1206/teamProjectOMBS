package com.jspiders.ombs.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jspiders.ombs.util.ErrorStructure;



public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{

	public ResponseEntity<ErrorStructure> emailAlreadyPresent(EmailException ex)
	{
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.ALREADY_REPORTED.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Email is already is present try with different email");
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.ALREADY_REPORTED);
	}
}
