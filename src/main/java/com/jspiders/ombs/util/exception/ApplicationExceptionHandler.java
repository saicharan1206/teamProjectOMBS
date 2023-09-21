package com.jspiders.ombs.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jspiders.ombs.util.ErrorStructure;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UserAlreadyExist.class)
	public ResponseEntity<ErrorStructure> saveUser(UserAlreadyExist alreadyExist, HttpServletRequest httpServletRequest){
		ErrorStructure error = new ErrorStructure();
		error.setMessage(alreadyExist.getMessage());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setRootCause(httpServletRequest.getRequestURI());
		
		
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorStructure> saveUser(UserNotFoundException usernotfound, HttpServletRequest httpServletRequest){
		ErrorStructure error = new ErrorStructure();
		error.setMessage(usernotfound.getMessage());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setRootCause(httpServletRequest.getRequestURI());
		
		
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(PasswordMissMatch.class)
	public ResponseEntity<ErrorStructure> saveUser(PasswordMissMatch missMatch, HttpServletRequest httpServletRequest){
		ErrorStructure error = new ErrorStructure();
		error.setMessage(missMatch.getMessage());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setRootCause(httpServletRequest.getRequestURI());
		
		
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(ProdcutNotFoundException.class)
	public ResponseEntity<ErrorStructure> updateuproduct(ProdcutNotFoundException productnotfound, HttpServletRequest httpServletRequest){
		ErrorStructure error = new ErrorStructure();
		error.setMessage(productnotfound.getMessage());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setRootCause(httpServletRequest.getRequestURI());
		
		
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		
	}
}
