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
import com.jspiders.ombs.serviceimpl.UserServiceImpl;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
	@Autowired
	UserServiceImpl userServiceImpl;

	@PostMapping("/saveuser")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(
			@RequestBody @Valid UserRequestDTO userRequestDTO) throws MessagingException {
		return userServiceImpl.saveUser(userRequestDTO);
	}
	
	@GetMapping("/getuser")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(
			 @Valid @RequestParam String email,
	    @RequestParam String password
	) {
	    return userServiceImpl.getUser(email, password);
	}
	
}
