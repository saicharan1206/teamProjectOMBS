package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/details")
@CrossOrigin
public class UserController {
	@Autowired
	private UserService service;

	@PostMapping("/{users}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody @Valid UserRequestDTO userRequestDTO)
			throws MessagingException {
		return service.saveUser(userRequestDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<String>> loginUser(@RequestParam String emailAddress,
			@RequestParam String password) {
		return service.loginUser(emailAddress, password);
	}
	@GetMapping("/new")
	public ResponseEntity<ResponseStructure<String>> newPassword(@RequestParam String emailAddress,@RequestParam String password) throws MessagingException
	{
		return service.newPassword(emailAddress, password);
	}
	
	@PostMapping("/forgot")
	public ResponseEntity<String> forgotPassword(@RequestParam String email) throws MessagingException
	{
		return service.forgotPassword(email);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(@RequestBody UserRequestDTO userRequestDTO,@PathVariable
			int userId) {
		return service.updateUser(userRequestDTO, userId);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(@PathVariable int userId)
	{
		return service.deleteUser(userId);
	}
	@GetMapping
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUser()
	{
		return service.findAllUser();
	}
	
	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO)
	{
		return service.saveProduct(productRequestDTO);
	}
	
	@PutMapping("/{pId}")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO,int pId)
	{
		return service.updateProduct(productRequestDTO,pId);
	}
	
	@DeleteMapping("/{pId}")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> deleteProduct(@PathVariable int pId)
	{
		return service.deleteProduct(pId);
	}
	
	@GetMapping("/{product}")
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> findAllProduct()
	{
		return service.findAllProduct();
	}
}
