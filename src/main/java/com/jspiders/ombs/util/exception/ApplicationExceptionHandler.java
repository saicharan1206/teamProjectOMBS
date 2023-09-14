package com.jspiders.ombs.util.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jspiders.ombs.util.ErrorStructure;

import jakarta.servlet.http.HttpServletRequest;
@RestControllerAdvice
public class ApplicationExceptionHandler 
{
	@ExceptionHandler(UserAlreadyExists.class)
	public ResponseEntity<?> handleUserAlreadyExist(UserAlreadyExists exception,HttpServletRequest request)
	{
		ErrorStructure error=new ErrorStructure();
		error.setMessage(exception.getMessage());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setRootCause(request.getRequestURI());
		error.setDateTime(LocalDateTime.now());
		
		
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotFoundByEmailException.class)
	public ResponseEntity<?> handleUserNotFoundByEmailException(UserNotFoundByEmailException exception,HttpServletRequest request)
	{
		ErrorStructure error=new ErrorStructure();
		error.setMessage(exception.getMessage());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setRootCause(request.getRequestURI());
		error.setDateTime(LocalDateTime.now());
		
		
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<?> handleInvalidPasswordException(InvalidPasswordException exception,HttpServletRequest request)
	{
		ErrorStructure error=new ErrorStructure();
		error.setMessage(exception.getMessage());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setRootCause(request.getRequestURI());
		error.setDateTime(LocalDateTime.now());
		
		
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}

}
