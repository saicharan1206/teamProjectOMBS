package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ForgotEmailResponse;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request);

	ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(String email, String password);

	ResponseEntity<ResponseStructure<ForgotEmailResponse>> sendforgotemail(String email);

	


}
