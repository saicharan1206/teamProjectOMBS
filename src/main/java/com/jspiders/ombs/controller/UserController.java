package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserEmailResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController 
{
	@Autowired
	private UserService service;
	
	@PostMapping("/create")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody  UserRequestDTO user) throws MessagingException
	{
		ResponseStructure<UserResponseDTO> responseStructure=service.saveUser(user);
		
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure,HttpStatus.CREATED);
	}
	
	@GetMapping("userEmail/{email}/{password}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getByEmail(@PathVariable String email,@PathVariable String password)
	{
		ResponseStructure<UserResponseDTO> responseStructure=service.getByEmail(email,password);
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure,HttpStatus.FOUND);
	}
	
	@GetMapping("userEmail/{email}")
	public ResponseEntity<ResponseStructure<UserEmailResponseDTO>> sendEmail(@PathVariable String email)throws MessagingException
	{
		ResponseStructure<UserEmailResponseDTO> responseStructure=service.sendEmail(email);
		return new ResponseEntity<ResponseStructure<UserEmailResponseDTO>>(responseStructure,HttpStatus.OK);
		
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> fetchAllAdmin()
	{
		ResponseStructure<List<UserResponseDTO>> responseStructure=service.fetchAllAdmin();
		return new ResponseEntity<ResponseStructure<List<UserResponseDTO>>>(responseStructure,HttpStatus.OK);
	}
	
	@DeleteMapping("delete/{email}")
	public ResponseEntity<ResponseStructure<String>> deletebymail(@PathVariable String email)
	{
		ResponseStructure<String> responseStructure=service.deletebymail(email);
		return new ResponseEntity<ResponseStructure<String>>(responseStructure,HttpStatus.OK);
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> update(@RequestBody UserRequestDTO userdata)
	{
		ResponseStructure<UserResponseDTO> responseStructure=service.update(userdata);
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure,HttpStatus.OK);
		
	}
	@PatchMapping("user/{email}/{pwd}/{cpwd}")
	public ResponseEntity<ResponseStructure<String>> updatePwd(@PathVariable String email,@PathVariable String pwd,@PathVariable String cpwd)
	{
		ResponseStructure<String> responseStructure=service.update(email,pwd,cpwd);
		return new ResponseEntity<ResponseStructure<String>>(responseStructure,HttpStatus.OK);
		
	}
	
 
}
