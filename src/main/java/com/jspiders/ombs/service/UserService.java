package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.LoginRequestDTO;
import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;


public interface UserService {

	public ResponseEntity<ResponseStructure> createUser(UserRequestDTO userDTO) throws MessagingException;
	
	public ResponseEntity<ResponseStructure> userLogin(LoginRequestDTO loginRequestDTO);


	ResponseEntity<String> sendMail(String to, String username, String role);
	
	public ResponseEntity<String> sendPasswordLink(String to);

}
