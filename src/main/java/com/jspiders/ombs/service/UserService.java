package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ForgotEmailResponse;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request);

	ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(String email, String password);

	ResponseEntity<ResponseStructure<ForgotEmailResponse>> sendforgotemail(String email) throws MessagingException;

	ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(String email);

	ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO request);

	ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findall();

	ResponseEntity<ResponseStructure<UserResponseDTO>> updatePassword(String password, String confirmpassword,
			String email);

	


}
