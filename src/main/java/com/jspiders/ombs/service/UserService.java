package com.jspiders.ombs.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;


public interface UserService {
	
/**
 * THIS METHOD IS TO SAVE THE USER DETAILS*/
	public  ResponseEntity<ResponseStructure<UserResponseDTO>> userSave(UserRequestDTO userRequest);
	/**
	 * THIS METHOD IS TO LOGIN USERDETAILS*/
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(UserRequestDTO userRequest);
	
	
	public ResponseEntity<ResponseStructure<String>> forgotPasswordValidation(String userEmail) throws MessagingException;
	
	/**
	 * THIS METHOD IS TO UPDATE THE DATA */
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO userRequestDTO,int userId);
	
	
	/**
	 * THIS METHOD IS TO DELETE THE DATA*/
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(int userId);
	
	/**
	 * THIS METHOD IS TO FIND ALL USER DETAILS*/
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUserDetails();

	/**
	 * THIS METHOD IS TO UPDATE THE USER PASSWORD*/
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updatePassword(String userPassword);
	
	
	
}

