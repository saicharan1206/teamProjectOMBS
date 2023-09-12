package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

@CrossOrigin
@RestController
@Validated
public class UserController {
	@Autowired 
	UserService service;
	
	@PostMapping("/user/create")
	public ResponseEntity<ResponseStructure> createUser(@RequestBody @Validated UserRequestDTO requestDTO) {
		return service.createUser(requestDTO);
	}
}
