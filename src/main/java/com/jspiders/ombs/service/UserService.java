package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {	
	
	// To SAVE user:
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest) throws MessagingException;
	
	// To FIND user:(log in)
	public ResponseEntity<ResponseStructure<String>> logInUser(String email, String password);
	
	// Forgot Password
	public ResponseEntity<String> changePassword(String email) throws MessagingException;
	
	// DELETE operation by ADMIN
	public ResponseEntity<String> submitUserId(int userId);
	
	// Confirm PASSWORD from User
	public ResponseEntity<ResponseStructure<String>> confirmDeleteMyAccount(int id, String password);
	
}
