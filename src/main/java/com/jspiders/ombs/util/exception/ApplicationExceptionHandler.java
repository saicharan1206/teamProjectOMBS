package com.jspiders.ombs.util.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jspiders.ombs.util.ErrorStructure;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
	
	

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		List<ObjectError> allErrors = ex.getAllErrors();
		Map<String, String> addErrors = new HashMap<>();
		for (ObjectError objectError : allErrors) {
			FieldError error = (FieldError)objectError;
			String message = error.getDefaultMessage();
			String field = error.getField();
			addErrors.put(field, message);
			
		}
		return new ResponseEntity(addErrors, HttpStatus.BAD_REQUEST);
	}

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
