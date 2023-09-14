package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.util.ResponseStructure;

public interface UserService {
	
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequestDTO userRequest);
	
	public ResponseEntity<ResponseStructure<UserResponse>> userlogin(UserRequestDTO userRequest);
}