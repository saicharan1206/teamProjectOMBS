package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;


@CrossOrigin
@RestController
public class UserController {
	@Autowired
	private UserService service;
	
	
	@PostMapping("/users")
	public  ResponseEntity<ResponseStructure<UserResponseDTO>> userSave(@RequestBody UserRequestDTO userRequest)  {
		return service.userSave(userRequest);
	}
	
	@PostMapping("/users/login")	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(@RequestBody UserRequestDTO userRequest) {
		return service.userLogin(userRequest);
	}
	
	@PostMapping("/users/{userEmail}")
	public ResponseEntity<ResponseStructure<String>> forgotPasswordValidation(@PathVariable String userEmail) throws MessagingException {
		return service.forgotPasswordValidation(userEmail);

	}
	
	
	@PutMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userUpdate(@RequestBody UserRequestDTO userRequestDTO ,@PathVariable int userId) {
		return service.updateUser(userRequestDTO, userId);

	}
	
	@DeleteMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(@PathVariable int userId) {
		return service.deleteUser(userId);

	}
	
	
	@GetMapping("/users")
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUserDetails()
	{
		return service.findAllUserDetails();
	}
	
	
	@PostMapping("/users/userPassword")
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updatePassword(@RequestParam("userPassword") String userPassword) {
		
		return service.updatePassword(userPassword);
	}
	
}
