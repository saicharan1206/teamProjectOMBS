package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO)throws MessagingException;

	public ResponseEntity<ResponseStructure<String>> loginUser(String emailaddress,String password);

	ResponseEntity<String> sendMail(MessageData messageData);
	
	public ResponseEntity<String> forgotPassword(String password)throws MessagingException;

	public ResponseEntity<String> myMail(MessageData messageData)throws MessagingException;
}
