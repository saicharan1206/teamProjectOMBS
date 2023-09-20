package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
	private UserService service;
	
	@CrossOrigin
	@PostMapping
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody @Valid UserRequestDTO userRequest)
																			throws MessagingException{
		return service.saveUser(userRequest);
	}
	
	/** GetMapping -> Incase of Using from Postman to Java Application **/
	/** PostMapping -> Incase of taking data from UI to Java Application **/
	
	@PostMapping("/login") 
	public ResponseEntity<ResponseStructure<String>> logInUser(@RequestParam String email,@RequestParam String password){
		return service.logInUser(email, password);
	}
	
	@PostMapping("/changePassword/{email}")
	public ResponseEntity<String> changePassword(@PathVariable String email) throws MessagingException {
		return service.changePassword(email);
	}
	
	@PostMapping("/deleteAccount")
	public ResponseEntity<ResponseStructure<String>> confirmDeleteMyAccount(@RequestParam int  id, String password) {
		return service.confirmDeleteMyAccount(id, password);
	}
	
	@PostMapping("/confirmNewPassword")
	public ResponseEntity<ResponseStructure<String>> confirmNewPassword(@RequestParam String newPassword){
		return service.confirmNewPassword(newPassword);
	}
	
}
