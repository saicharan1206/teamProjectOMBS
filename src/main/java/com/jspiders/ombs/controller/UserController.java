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
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.ForgotEmailResponse;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.validation.Valid;


@CrossOrigin
@RestController
public class UserController {
	@Autowired
	private UserService service;
	@PostMapping("/user")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody @Valid UserRequestDTO requestDTO)
	{
		return service.saveUser(requestDTO);
		
	}
	
	@GetMapping("/user/{email}/{password}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(@PathVariable String email,
			@PathVariable String password) {
		return service.getUser(email, password);
	}
	@GetMapping("/userEmail/{email}")
	public ResponseEntity<ResponseStructure<ForgotEmailResponse>> getUserByEmail(@PathVariable String email) {
		return service.getUserByEmail(email);
	}
	
	@DeleteMapping("/deleteuser/{email}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteByEmail(@PathVariable String email){
		return service.deleteByEmail(email);
	}
	
	@PutMapping("/updateuser")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateByEmail(@RequestBody @Valid UserRequestDTO requestDTO){
		return service.updateByEmail(requestDTO);
	}
	
	@GetMapping("/fetchuser")
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUser()
	{
		return service.findAllUser();
	}
}

