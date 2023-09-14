package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.LoginResponse;
import com.jspiders.ombs.dto.LoginVerification;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

public interface UserService {

//	This method is used to save the user to the database
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData (UserRequestDTO userRequestDTO);
	
	public ResponseEntity<ResponseStructure<LoginResponse>> loginVer (LoginVerification loginVerification);
	
}
