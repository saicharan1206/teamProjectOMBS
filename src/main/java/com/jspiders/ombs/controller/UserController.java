package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/users")
@CrossOrigin
public class UserController {
	@Autowired
	private UserService service;

	@PostMapping
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody @Valid UserRequestDTO userRequestDTO)
			throws MessagingException {
		return service.saveUser(userRequestDTO);
	}

	@GetMapping("/login")
	public ResponseEntity<ResponseStructure<String>> loginUser(@RequestParam String emailAddress,
			@RequestParam String password) {
		return service.loginUser(emailAddress, password);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(@RequestBody UserRequestDTO userRequestDTO,@PathVariable
			int userId) {
		return service.updateUser(userRequestDTO, userId);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(@PathVariable int userId)
	{
		return service.deleteUser(userId);
	}
	@GetMapping
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUser()
	{
		return service.findAllUser();
	}
}
