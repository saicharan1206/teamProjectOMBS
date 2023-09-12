package com.jspiders.ombs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jspiders.ombs.util.ErrorStructure;


/*
 * @RestControllerAdvice annotated class accepts all exceptions and Handle the same exception
 */

@RestControllerAdvice
public class ApplicationExceptionHandler {

	/*
	 * This method accepting UserWithSameEmailExist exception and Handling the same exception 
	 * by returning proper ErrorStructure 
	 */
	@ExceptionHandler(UserWithSameEmailExist.class)
	public ResponseEntity<ErrorStructure> emailAlreadyExist(UserWithSameEmailExist ex) {
		
		ErrorStructure errorStructure = new ErrorStructure();
		errorStructure.setMessage(ex.getMessage());
		errorStructure.setRootCause("User email already in database");
		errorStructure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		
	return new ResponseEntity<ErrorStructure>(errorStructure,HttpStatus.NOT_ACCEPTABLE);
	}
}
