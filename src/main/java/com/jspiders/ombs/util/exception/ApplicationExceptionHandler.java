package com.jspiders.ombs.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jspiders.ombs.util.ErrorStructure;

public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> emailAlreadyPresent(EmailException ex)
	{
		ErrorStructure structure=new ErrorStructure();
		structure.setStatusCode(HttpStatus.ALREADY_REPORTED.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Email is already present it will not accept duplicate email");
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.ALREADY_REPORTED);
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> emailNotFound(EmailNotFoundException ex){
		ErrorStructure structure=new ErrorStructure();
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Email is not found");

		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.NOT_FOUND);

	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> passwordNotMatching(PasswordNotMatchingException ex){
		ErrorStructure structure=new ErrorStructure();
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Password not matching");
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.NOT_FOUND);
	}
}
