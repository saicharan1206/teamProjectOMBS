package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.ForgotEmailResponse;
import com.jspiders.ombs.dto.LoginDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class UserController {
	@Autowired
	private UserService service;

	@PostMapping("/users")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody @Valid UserRequestDTO requestDTO) {
		return service.saveUser(requestDTO);
	}

	@PostMapping("/users/login")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUserByEmailByPassword(@RequestBody @Valid LoginDTO login) {
		return service.getUserByEmailByPassword(login);
	}

	@GetMapping("/users/{useremail}")
	public ResponseEntity<ResponseStructure<ForgotEmailResponse>> sendConfirmationEntityMail(@PathVariable String useremail) throws MessagingException {
		return service.sendConfirmationEntityMail(useremail);
	}

	@DeleteMapping("/users/{useremail}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteByEmail(@PathVariable String useremail) {
		return service.deleteByEmail(useremail);
	}

	@PutMapping("/users")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(
			@RequestBody @Valid UserRequestDTO requestDTO) {
		return service.updateByEmail(requestDTO);
	}

	@GetMapping("/users")
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> getAllUsers() {
		return service.getAllUsers();
	}
	
	@PatchMapping("/users/reset/{email}/{pwd}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> resetPassword(@PathVariable String email,@PathVariable String pwd ) {
		return service.resetPassword(email,pwd);
	}
}
