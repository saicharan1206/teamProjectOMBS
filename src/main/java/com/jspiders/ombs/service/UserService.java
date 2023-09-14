package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.validation.Valid;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@Valid UserRequestDTO requestDTO);

	ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(String email, String password);

}