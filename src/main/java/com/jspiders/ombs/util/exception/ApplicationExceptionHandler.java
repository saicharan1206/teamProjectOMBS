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

		return new ResponseEntity<ErrorStructure>(error, HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userNotFoundByEmailException(UserNotFoundByEmailException ex)
	{
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setRootCause("User not found by this mail id");
		error.setStatusCode(HttpStatus.NOT_FOUND.value());

		return new ResponseEntity<ErrorStructure>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> wrongPasswordException (WrongPasswordException ex)
	{
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setRootCause("Password is wrong !!");
		error.setStatusCode(HttpStatus.FORBIDDEN.value());

		return new ResponseEntity<ErrorStructure>(error, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userNotFoundByIdException (UserNotFoundByIdException ex)
	{
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setRootCause("User not present with this id !!");
		error.setStatusCode(HttpStatus.NOT_FOUND.value());

		return new ResponseEntity<ErrorStructure>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> youAreNotAllowedException (YouAreNotAllowedException ex)
	{
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setRootCause("Not Allowed to make changes here !!");
		error.setStatusCode(HttpStatus.FORBIDDEN.value());

		return new ResponseEntity<ErrorStructure>(error, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userRoleNotFoundException (UserRoleNotFoundException ex)
	{
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setRootCause("UserRole not found !!");
		error.setStatusCode(HttpStatus.NOT_FOUND.value());

		return new ResponseEntity<ErrorStructure>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> nullPointerException (NullPointerException ex)
	{
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setRootCause("No users working as this UserRole !!");
		error.setStatusCode(HttpStatus.NOT_FOUND.value());

		return new ResponseEntity<ErrorStructure>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> passwordMissmatchException (PasswordMissmatchException ex)
	{
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setRootCause("Password missmatch !!");
		error.setStatusCode(HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<ErrorStructure>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> createNewPasswordExceptOldPassword (CreateNewPasswordExceptOldPassword ex)
	{
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setRootCause("cretae new password instead of old password !!");
		error.setStatusCode(HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<ErrorStructure>(error, HttpStatus.BAD_REQUEST);
	}

}
