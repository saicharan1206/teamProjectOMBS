package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {

	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest)throws MessagingException;
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(UserRequestDTO userRequest) throws MessagingException;
	
    public ResponseEntity<String> changePassword(String password) throws MessagingException;
    
    public ResponseEntity<ResponseStructure< String>> isDeleted(int userId,String userPassword);
}
