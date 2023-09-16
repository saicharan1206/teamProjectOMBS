package com.jspiders.ombs.service;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserNotFoundException;

import jakarta.mail.MessagingException;


public interface UserService {
	/**
	 * This method is used to save the user to the database 
	 * @throws MessagingException */
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequestDTO userRequestDTO) throws MessagingException;
	
	public ResponseEntity<ResponseStructure<UserResponse>> getUser(String email, String password) throws UserNotFoundException;
	
	public ResponseEntity<String> sendMimeMeassage(String userEmail, String role) throws MessagingException;
	
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(String email) throws UserNotFoundException;

	public ResponseEntity<ResponseStructure<UserResponse>> updateUser(String email, UserRequestDTO userRequestDTO)
			throws UserNotFoundException;

	public ResponseEntity<ResponseStructure<List<UserResponse>>> getAllUsers();


	

	
	


}
