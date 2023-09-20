package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;
	
	@CrossOrigin
	@PostMapping
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(@RequestBody @Valid UserRequestDTO userRequest)  throws MessagingException{
		return service.saveUser(userRequest);
	}
	
//	@GetMapping
//	public ResponseEntity<ResponseStructure<UserResponse>> userlogin(@RequestBody String userEmail, String userPassord){
//		return service.userlogin(userEmail, userPassord);
//	}
	
	@CrossOrigin
	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<UserResponse>> userlogin(@RequestBody UserRequestDTO userRequest){
		return service.userlogin(userRequest);
	}
	
	@CrossOrigin
	@PostMapping(params =  "email")
	public ResponseEntity<String> changePassword(@RequestParam String email) throws MessagingException{
		return service.changePassword(email);
	}
	
	
	@CrossOrigin
	@PutMapping("/softdelete")
	public ResponseEntity<ResponseStructure<String>> deleteUser(@RequestParam int userId, String password){
		return service.deleteUser(userId, password );
	}

}
