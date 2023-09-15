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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.serviceimpl.UserServiceImpl;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserNotFoundException;

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
	
	@GetMapping("/getuser/{email}/{password}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(@PathVariable String email,
			@PathVariable String password) throws UserNotFoundException {
		return userServiceImpl.getUser(email, password);
	}
	
	@DeleteMapping("/delete")
    public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(
        @RequestHeader("email") String email) throws UserNotFoundException {
        return userServiceImpl.deleteUser(email);
    }

	 @PutMapping("/{email}")
	    public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(@PathVariable String email, @RequestBody UserRequestDTO userRequestDTO) throws UserNotFoundException {
     return userServiceImpl.updateUser(email, userRequestDTO);
	 }
	 
	 @GetMapping("/allusers")
	    public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> getAllUsers() {
	        return userServiceImpl.getAllUsers();
	    }
}
