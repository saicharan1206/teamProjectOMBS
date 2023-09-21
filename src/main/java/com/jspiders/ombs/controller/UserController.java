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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService service;

//	enables cross-origin resource sharing only for this specific method.
//	By default, its allows all origins, all headers, and the HTTP methods specified in the @RequestMapping annotation.A
	@CrossOrigin
	@PostMapping
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody UserRequestDTO userRequest)throws MessagingException {
		return service.saveUser(userRequest);
	}

	@CrossOrigin
	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(@RequestBody UserRequestDTO userRequest)
			throws MessagingException {
		return service.userLogin(userRequest);

	}
	@CrossOrigin
	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody String password) throws MessagingException{
		return service.changePassword(password);
		
	}
    @CrossOrigin
	@PostMapping("/users/deleted")
	public ResponseEntity<ResponseStructure<String>> isDeleted(@RequestParam int userId, String userPassword) {
		return service.isDeleted(userId, userPassword);

	}
    @CrossOrigin
	@PostMapping("/users/newPassword")
	public ResponseEntity<ResponseStructure<String>> confirmPassword(@RequestParam String userEmail, String newPassword){
		return service.confirmPassword(userEmail, newPassword);
	}

}
