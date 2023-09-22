package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ForgotEmailResponse;
import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

public interface UserService {

	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest);
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(UserRequestDTO userRequest);

	public ResponseEntity<ResponseStructure<String>> forgotPasswordValidation(String userEmail)
	                 throws MessagingException;
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO userRequestDTO,
			        int userId);
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(int userId);
	
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUsers();
	
    public ResponseEntity<String> sendMail(MessageData messageData);
	
	public ResponseEntity<String> sendMimeMessage(MessageData messageData) 
			                throws MessagingException;
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(String email, String password);

	public ResponseEntity<ResponseStructure<ForgotEmailResponse>> getUserByEmail(String email) throws MessagingException;

	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteByEmail(String email);

	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateByEmail(@Valid UserRequestDTO requestDTO);

	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUser();

	public ResponseEntity<ResponseStructure<UserResponseDTO>> updatePassword(String email, String pwd,
			String confirmPwd);

}
