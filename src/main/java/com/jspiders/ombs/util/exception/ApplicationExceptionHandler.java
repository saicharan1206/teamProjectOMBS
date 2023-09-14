package com.jspiders.ombs.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jspiders.ombs.util.ErrorStructure;




@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> emailAlreadyExists(EmailAlreadyExistsException ex){
		ErrorStructure structure = new ErrorStructure();
		
		structure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause(ex.getEmail()+" Email already exists " );
		return new ResponseEntity<ErrorStructure> (structure, HttpStatus.NOT_ACCEPTABLE);
	} 
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userNotFoundById(UserNotFoundByIdException ex){
		ErrorStructure structure = new ErrorStructure();
		
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("User is not found with requested Id " +ex.getId());
		return new ResponseEntity<ErrorStructure> (structure, HttpStatus.NOT_FOUND);
		
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> passwordMismatch(PasswordMismatchException ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Invalid credentials");
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.NOT_ACCEPTABLE );
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> emailDoesnotExists(EmailDoesnotExistsException ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("User is not found with requested email ");
		return new ResponseEntity<ErrorStructure> (structure, HttpStatus.NOT_FOUND);
		
		
	}
	

}
