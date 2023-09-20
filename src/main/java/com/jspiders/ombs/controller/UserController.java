package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

@RestController
public class UserController {
	@Autowired
	private UserService service;

//	enables cross-origin resource sharing only for this specific method.
//	By default, its allows all origins, all headers, and the HTTP methods specified in the @RequestMapping annotation.A
	@CrossOrigin
	@PostMapping("/users/adduser")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody UserRequestDTO userRequest)
			throws MessagingException {
		return service.saveUser(userRequest);
	}

	@CrossOrigin
	@PostMapping("/users/login")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(@RequestBody UserRequestDTO userRequest)
			throws MessagingException {
		return service.userLogin(userRequest);

	}

	@PostMapping("/users/deleted")
	public ResponseEntity<ResponseStructure<String>> isDeleted(@RequestParam int userId, String userPassword) {
		return service.isDeleted(userId, userPassword);

	}

}
