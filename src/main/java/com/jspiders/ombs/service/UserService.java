package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ForgotEmailResponse;
import com.jspiders.ombs.dto.LoginDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@Valid UserRequestDTO requestDTO);

	ResponseEntity<ResponseStructure<UserResponseDTO>> getUserByEmailByPassword(LoginDTO login);

	ResponseEntity<ResponseStructure<ForgotEmailResponse>> sendConfirmationEntityMail(String email) throws MessagingException;

	ResponseEntity<ResponseStructure<UserResponseDTO>> deleteByEmail(String email);

	ResponseEntity<ResponseStructure<UserResponseDTO>> updateByEmail(@Valid UserRequestDTO requestDTO);

	ResponseEntity<ResponseStructure<List<UserResponseDTO>>> getAllUsers();

	ResponseEntity<ResponseStructure<UserResponseDTO>> resetPassword(String email, String pwd);



}
