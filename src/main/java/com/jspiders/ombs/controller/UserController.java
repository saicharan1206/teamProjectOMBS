package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;
	
	@PostMapping("/create")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody UserRequestDTO userRequest)
	{
		return service.saveUser(userRequest);
	}
	
	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(@RequestBody UserRequestDTO userRequest)
	{
		return service.userLogin(userRequest);
	}
	
	@PostMapping("/{userEmail}")
	public ResponseEntity<ResponseStructure<String>> forgotPasswordValidation(@PathVariable String userEmail)
	                     throws MessagingException
	{
		return service.forgotPasswordValidation(userEmail);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(@RequestBody UserRequestDTO userRequestDTO, @PathVariable int userId)
	{
		return service.updateUser(userRequestDTO,userId);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(@PathVariable int userId)
	{
		return service.deleteUser(userId);
	}
	
	@GetMapping("/users")
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUsers()
	{
		return service.findAllUsers();
	}
	
}
