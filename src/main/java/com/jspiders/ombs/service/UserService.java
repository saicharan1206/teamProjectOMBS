package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ForgotRequest;
import com.jspiders.ombs.dto.LoginRequest;
import com.jspiders.ombs.dto.LoginResponse;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;



public interface UserService 
{
	/**
	 *This method is used to save the user to the database*/
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData(UserRequestDTO user);
	
	public ResponseEntity<ResponseStructure<LoginResponse>> loginUser(LoginRequest login);
	
	public ResponseEntity<String> forgotPassword(ForgotRequest forgot);
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO user, int userId);
	
	public ResponseEntity<ResponseStructure<LoginResponse>> deleteAccount(int userId);
	
	
}
