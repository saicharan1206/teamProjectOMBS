package com.jspiders.ombs.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jspiders.ombs.util.ErrorStructure;


@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler
	public ResponseEntity<ErrorStructure> emailAlreadyPresent(EmailException ex)
	{
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.ALREADY_REPORTED.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Email is already is present try with different email");
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.ALREADY_REPORTED);
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> passwordNotMatch(PasswordDidNotMatchException ex1)
	{
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.ALREADY_REPORTED.value());
		structure.setMessage(ex1.getMessage());
		structure.setRootCause("Password Did Not Match try with Vaild Password");
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.ALREADY_REPORTED);
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> emailDidNotFound(EmailNotFound ex)
	{
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.ALREADY_REPORTED.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Email does not exist");
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.ALREADY_REPORTED);
	}
}
