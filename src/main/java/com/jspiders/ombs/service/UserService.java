package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;



public interface UserService {
	/**
	 * This method is to save user data*/
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request);
	/**
	 * This method is used to update the data */
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO request, int userID);

}
