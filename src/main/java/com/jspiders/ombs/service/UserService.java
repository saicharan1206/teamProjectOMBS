package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

public interface UserService {
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO);
	
	public ResponseEntity<ResponseStructure<String>> userLogin(String userEmail, String userPassword);
	
	/** we can do login using UserRequestDTO also */
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin2(UserRequestDTO userRequestDTO);

}

