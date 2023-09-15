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
			
		List<ObjectError> allErrors=ex.getAllErrors();
		 Map<String, String> errors = new HashMap<String, String>();
		 
		 for (ObjectError error : allErrors) { 
			 FieldError fieldError = (FieldError) error;  // FieldError extends ObjectError
			 String errorMessage = fieldError.getDefaultMessage();
			 String fieldName = fieldError.getField();
			 errors.put(fieldName, errorMessage);
		 }
		
		return new ResponseEntity<Object>(errors,HttpStatus.BAD_REQUEST);
		}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userEmailAlreadyFound(EmailAlreadyExistException exc){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.FOUND.value());
		structure.setMessage(exc.getMessage());
		structure.setRootCause("This Email Aready found!!!");
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userNotFoundByEmail(UserNotFoundByEmailException ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setMessage("User doesnot found!!!");
		structure.setRootCause("User with requested Email doesnot exist!!!");
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userNotFoundById(UserNotFoundByIdException ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setMessage("User Account is not deleted!!!");
		structure.setRootCause("User with requested id doesnot exist!!!!");
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> incorrectPassword(IncorrectPasswordException ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		structure.setMessage("User Account is not deleted!!!");
		structure.setRootCause("You have entered incorrect password, Please enter correct password!!!!");
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.NOT_ACCEPTABLE);
	}

}








