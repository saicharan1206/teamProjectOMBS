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
	
	/** After extending 'ResponseEntityExceptionHandler' class, 
 	type handleMethodArgumentNotValid this will give you the Exception Implementation**/
	
	@Override
		protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
				HttpHeaders headers, HttpStatusCode status, WebRequest request) {
			
		List<ObjectError> allErrors = ex.getAllErrors(); // UPCASTING : ObjectError is the SuperClass of FieldError which extends Error Class.
		
		Map<String, String> mapErrors = new HashMap<String, String>();
		
		for(ObjectError error : allErrors) {
			FieldError fieldError = (FieldError)error;  // DOWNCASTING : ObjectError -> FieldError
			String message = fieldError.getDefaultMessage(); /** VALUE **/
			String field = fieldError.getField(); /** KEY **/
			
			mapErrors.put(field,message);
		}
		return new ResponseEntity<Object>(mapErrors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userEmailAlreadyFound(EmailAlreadyFoundException exp){
		ErrorStructure error = new ErrorStructure();
		error.setStatusCode(HttpStatus.FOUND.value());
		error.setMessage(exp.getMessage());
		error.setRootCause("This email is already exists !!");
		
		return new ResponseEntity<ErrorStructure>(error , HttpStatus.FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userRoleNotAccepted(RoleNotApplicableException exp){
		ErrorStructure error = new ErrorStructure();
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setMessage(exp.getMessage());
		error.setRootCause("Role is NOT applicable !!");
		
		return new ResponseEntity<ErrorStructure>(error , HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userEmailNotExists(UserNotFoundByEmailException exp){
		ErrorStructure error = new ErrorStructure();
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setMessage(exp.getMessage());
		error.setRootCause("Email not exists!!");
		
		return new ResponseEntity<ErrorStructure>(error , HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userIdNotExists(UserNotFoundByIdException exp){
		ErrorStructure error = new ErrorStructure();
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setMessage(exp.getMessage());
		error.setRootCause("Id not exists!!");
		
		return new ResponseEntity<ErrorStructure>(error , HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> invalidPassword(IncorrectPasswordException exp){
		ErrorStructure error = new ErrorStructure();
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setMessage(exp.getMessage());
		error.setRootCause("Password is mismatched with your email");
		
		return new ResponseEntity<ErrorStructure>(error , HttpStatus.NOT_FOUND);
	}
}
