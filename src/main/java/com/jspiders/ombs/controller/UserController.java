package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	private UserService userService;
	
	@CrossOrigin
	@PostMapping
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody @Valid UserRequestDTO userRequestDTO) throws MessagingException{
		return userService.saveUser(userRequestDTO);
	}
	
	@CrossOrigin
//	@PostMapping("/login") // for UI
	@GetMapping
	public ResponseEntity<ResponseStructure<String>> login(@RequestParam String email, String password){
		return userService.userLogin(email, password);
	}
	
	@CrossOrigin
	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestParam String email) throws MessagingException{
		return userService.changePassword(email);
	}
	

}




