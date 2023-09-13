package com.jspiders.ombs.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jspiders.ombs.util.ErrorStructure;


@RestControllerAdvice
public class ApplicationExceptionHandler {
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userEmailAlreadyFound(UserNotFoundByIdException exc){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.FOUND.value());
		structure.setMessage(exc.getMessage());
		structure.setRootCause("This Email Aready found!!!");
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.FOUND);
	}

}
