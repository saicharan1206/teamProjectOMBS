package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	private UserService service;

	
	@PostMapping("/saveuser")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody UserRequestDTO request) {
		return service.saveUser(request);
	}

	
	@GetMapping("/getuser/{email}/{password}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(@PathVariable String email,
			@PathVariable String password) {
		return service.getUser(email, password);
	}
}
