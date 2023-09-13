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

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		List<ObjectError> allErrors = ex.getAllErrors();
		Map<String, String> errors = new HashMap<String, String>();

		for (ObjectError er : allErrors) {
			FieldError fieldError = (FieldError) er;
			String message = fieldError.getDefaultMessage();
			String field = fieldError.getField();
			errors.put(field, message);
		}

		return super.handleMethodArgumentNotValid(ex, headers, status, request);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userAlreadyExistsException(UserAlreadyExistsException ex) {
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setRootCause("User Email is already exists");
		error.setStatusCode(HttpStatus.ALREADY_REPORTED.value());

		return new ResponseEntity<>(error, HttpStatus.ALREADY_REPORTED);
	}

}
