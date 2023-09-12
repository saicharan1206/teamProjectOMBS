package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.serviceimpl.UserServiceImpl;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserServiceImpl userServiceImpl;
	@PostMapping("/save")
	@CrossOrigin
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody @Valid UserRequestDTO userRequestDTO){
		return userServiceImpl.saveUser(userRequestDTO);
	}
}
