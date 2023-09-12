package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

@Service
public interface UserService {
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO user);

}
