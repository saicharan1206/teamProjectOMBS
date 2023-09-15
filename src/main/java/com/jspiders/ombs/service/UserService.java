package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO)throws MessagingException;

	public ResponseEntity<ResponseStructure<String>> loginUser(String emailAddress,String password);
	
	public ResponseEntity<String> forgotPassword(String password)throws MessagingException;
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO userRequestDTO,int userId);
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(int userId);
	
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUser();
}
