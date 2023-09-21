package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@PostMapping("/idToDeleteAccount")
	public ResponseEntity<ResponseStructure<String>> idToDeleteAccount(@RequestParam int userId){
		return userService.enterIdToDeleteAccount(userId);
	}
	
	@PostMapping("/passwordToDeleteAccount")
	public ResponseEntity<ResponseStructure<String>> passwordToDeleteAccount(@RequestParam String password){
		return userService.enterPasswordToDelete(password);
	}
	
	@PostMapping("/deleteAccount2")
	public ResponseEntity<ResponseStructure<String>> deleteAccount2(@RequestParam int userId, String userPassword){
		return userService.deleteUserAccount2(userId, userPassword);
	}
	
	@GetMapping("/createNewPassword")
	public ResponseEntity<ResponseStructure<String>> createNewPassword(@RequestParam String password){
		return userService.createNewPassword(password);
	}
	
//	-------------------
	@GetMapping("/retrieve")
    public ResponseEntity<String> retrieveEmail(@RequestParam String token) {
		return userService.retrieveEmail(token);
	}
	

}










