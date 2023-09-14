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

import com.jspiders.ombs.dto.UserLoginDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;


@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserService service;
	
	@PostMapping
	@CrossOrigin
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody  UserRequestDTO request){
		return service.saveUser(request);
	}
	
	@PutMapping(params = "userId")
	@CrossOrigin
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(@RequestBody  UserRequestDTO request, @RequestParam int userId){
		return service.updateUser(request, userId);
	}
	
	@PostMapping("/login")
	@CrossOrigin
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(@RequestBody UserLoginDTO login){
		return service.userLogin(login);
	}
	
	@PostMapping("/{email}")
	@CrossOrigin
	public ResponseEntity<ResponseStructure<String>> passwordReset(@PathVariable String email){
		return service.passwordReset(email);
	}
		
	

}
