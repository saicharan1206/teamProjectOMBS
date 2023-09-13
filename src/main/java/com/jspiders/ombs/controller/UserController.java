package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;


@CrossOrigin
@RestController
public class UserController {
	@Autowired
	private UserService service;
	
	
	@PostMapping("/users")
	public  ResponseEntity<ResponseStructure<UserResponseDTO>> userSave(@RequestBody UserRequestDTO userRequest)  {
		return service.userSave(userRequest);



}
}
