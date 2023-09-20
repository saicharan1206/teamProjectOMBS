package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {
	
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequestDTO userRequest)  throws MessagingException;
	
	public ResponseEntity<ResponseStructure<UserResponse>> userlogin(UserRequestDTO userRequest);
	
	public ResponseEntity<String> changePassword(String password) throws MessagingException;
	
	public ResponseEntity<ResponseStructure<String>> deleteUser(int userID, String password);
	
	
	
}