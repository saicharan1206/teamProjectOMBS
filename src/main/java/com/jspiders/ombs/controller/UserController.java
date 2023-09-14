package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserEmailResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController 
{
	@Autowired
	private UserService service;
	
	@PostMapping("/create")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody UserRequestDTO user) throws MessagingException
	{
		ResponseStructure<UserResponseDTO> responseStructure=service.saveUser(user);
		
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure,HttpStatus.CREATED);
	}
	@GetMapping("userEmail/{email}/{password}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getByEmail(@PathVariable String email,@PathVariable String password)
	{
		ResponseStructure<UserResponseDTO> responseStructure=service.getByEmail(email,password);
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure,HttpStatus.FOUND);
	}
	@GetMapping("userEmail/{email}")
	public ResponseEntity<ResponseStructure<UserEmailResponseDTO>> sendEmail(@PathVariable String email)throws MessagingException
	{
		ResponseStructure<UserEmailResponseDTO> responseStructure=service.sendEmail(email);
		return new ResponseEntity<ResponseStructure<UserEmailResponseDTO>>(responseStructure,HttpStatus.OK);
		
	}
	


}
