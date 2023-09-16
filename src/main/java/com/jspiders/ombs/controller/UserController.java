package com.jspiders.ombs.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserNotFoundException;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService service;
	
	@PostMapping("/saveUser")
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(@RequestBody @Valid UserRequestDTO userRequestDTO) throws MessagingException {
		return service.saveUser(userRequestDTO);
	}
	
	@GetMapping("/getuser/{email}/{password}")
	public ResponseEntity<ResponseStructure<UserResponse>> getUser(@PathVariable String email,
			@PathVariable String password) throws UserNotFoundException {
		return service.getUser(email, password);
	}
	
	@DeleteMapping("/delete")
    public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(@RequestHeader("email") String email) throws UserNotFoundException {
        return service.deleteUser(email);
    }

	 @PutMapping("/{email}")
	    public ResponseEntity<ResponseStructure<UserResponse>> updateUser(@PathVariable String email, @RequestBody UserRequestDTO userRequestDTO) throws UserNotFoundException {
     return service.updateUser(email, userRequestDTO);
	 }
	 
	 @GetMapping("/allusers")
	    public ResponseEntity<ResponseStructure<List<UserResponse>>> getAllUsers() {
	        return service.getAllUsers();
	    }
}
