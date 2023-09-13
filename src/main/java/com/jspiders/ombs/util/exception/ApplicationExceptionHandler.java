package com.jspiders.ombs.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jspiders.ombs.util.ErrorStructure;


@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler  {
	
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> emailAlreadyExist(EmailAlreadyExistException exception)
	{
		
		ErrorStructure structure = new ErrorStructure();
		structure.setMessage(exception.getMessage());
		structure.setRootCause("EMAIL ALREADY EXIST SO CREATE A UNIQUE EMAIL ID");
		structure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.NOT_ACCEPTABLE);
		
	}
	
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userNotFoundByiD(UserNotFoundByIdException exception)
	{
		
		ErrorStructure structure = new ErrorStructure();
		structure.setMessage(exception.getMessage());
		structure.setRootCause("STUDENT IS NOT PRESENT");
		structure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.NOT_ACCEPTABLE);
		
	}
	
	
}
