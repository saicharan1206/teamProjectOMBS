package com.jspiders.ombs.service;




import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {
	/**
	 * This method is used to save the user to the database
	 * @throws MessagingException 
	 */
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO) throws MessagingException;
	
	ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(String email, String password);
	
}
