package com.jspiders.ombs.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jspiders.ombs.util.ErrorStructure;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApplicationExceptionHandler 
{
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> saveUser(UserAlreadyExist exist, HttpServletRequest httpServletRequest){
		ErrorStructure error = new ErrorStructure();
		error.setMessage(exist.getMessage());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setRootCause(httpServletRequest.getRequestURI());
		
		
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		
	}
}
