package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.util.ResponseStructure;


public interface UserService {
	/**
	 * This method is used to save the user to the database */
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequestDTO userRequestDTO);


}
