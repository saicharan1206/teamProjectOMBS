package com.jspiders.ombs.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jspiders.ombs.util.ErrorStructure;


@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(emailIdAlreadyPresentException.class)
	public ResponseEntity<ErrorStructure> emailAlreadyExist(emailIdAlreadyPresentException eap) {
		ErrorStructure errorStructure = new ErrorStructure();
		errorStructure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		errorStructure.setRootCause("email already present");
		errorStructure.setMessage(eap.getMessage());
		
		return new ResponseEntity<ErrorStructure>(errorStructure,HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<ErrorStructure> emailNotFound(EmailNotFoundException enf) {
		ErrorStructure errorStructure = new ErrorStructure();
		errorStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorStructure.setRootCause("email not present in database");
		errorStructure.setMessage(enf.getMessage());
		
		return new ResponseEntity<ErrorStructure>(errorStructure,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<ErrorStructure> passwordMismatch(PasswordMismatchException pmm) {
		ErrorStructure errorStructure = new ErrorStructure();
		errorStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorStructure.setRootCause("This Password Does Not Match with any passwords in database");
		errorStructure.setMessage(pmm.getMessage());
		
		return new ResponseEntity<ErrorStructure>(errorStructure,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotFoundByIdException.class)
	public ResponseEntity<ErrorStructure> userNotFoundById(UserNotFoundByIdException unf) {
		ErrorStructure errorStructure = new ErrorStructure();
		errorStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorStructure.setRootCause("This User Id is not found in database");
		errorStructure.setMessage(unf.getMessage());
		
		return new ResponseEntity<ErrorStructure>(errorStructure, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CannotUseSamePasswordException.class)
	public ResponseEntity<ErrorStructure> cannotUseSamePassword(CannotUseSamePasswordException cusp){
		ErrorStructure errorStructure = new ErrorStructure();
		errorStructure.setStatusCode(HttpStatus.CONFLICT.value());
		errorStructure.setRootCause("New password cannot be same as old password");
		errorStructure.setMessage(cusp.getMessage());
		
		return new ResponseEntity<ErrorStructure>(errorStructure, HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(NotAuthorizedToAddProductException.class)
	public ResponseEntity<ErrorStructure> notAuthorizedToAddProduct(NotAuthorizedToAddProductException natp){
		ErrorStructure errorStructure = new ErrorStructure();
		errorStructure.setStatusCode(HttpStatus.FORBIDDEN.value());
		errorStructure.setRootCause("User with role customer cannot add product");
		errorStructure.setMessage(natp.getMessage());
		
		return new ResponseEntity<ErrorStructure>(errorStructure, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ProductAlreadyPresentException.class)
	public ResponseEntity<ErrorStructure> productAlreadyPresent(ProductAlreadyPresentException pap){
		ErrorStructure errorStructure = new ErrorStructure();
		errorStructure.setStatusCode(HttpStatus.CONFLICT.value());
		errorStructure.setRootCause("Cannot add the same product again");
		errorStructure.setMessage(pap.getMessage());
		
		return new ResponseEntity<ErrorStructure>(errorStructure, HttpStatus.CONFLICT);
	}
}
