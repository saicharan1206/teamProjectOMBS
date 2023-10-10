package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.LoginRequestDTO;
import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.entity.Products;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.service.ProductService;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

@CrossOrigin
@RestController
@Validated
public class UserController {
	@Autowired 
	UserService service;
	
	@PostMapping("/user/create")
	public ResponseEntity<ResponseStructure> createUser(@RequestBody @Validated UserRequestDTO requestDTO) throws MessagingException {
		return service.createUser(requestDTO);
	}
	
	@GetMapping("/user/login")
	public ResponseEntity<ResponseStructure> userLogin(@RequestBody @Validated  LoginRequestDTO loginRequestDTO) {
		return service.userLogin(loginRequestDTO);
	}
	
	@PostMapping("/user/forgotpwd")
	public ResponseEntity<ResponseStructure> sendPasswordLink(@RequestParam String to) {
		
		String resetToken = service.generateResetToken();
		return service.sendPasswordLink(to,resetToken);
		
	}
	
	@PutMapping("/user/reset")
	public ResponseEntity<ResponseStructure> resetPassword(@RequestParam String email, @RequestParam String newPassword, @RequestParam String confirmPwd) {
		return service.resetPassword(email, newPassword, confirmPwd);
	}
	
	@GetMapping("/user/findbyId/{userId}")
	public ResponseEntity<ResponseStructure> findUserById(@PathVariable Integer userId){
		return service.findUserById(userId);
	}
	
	@GetMapping("/user/findAdmin")
	public List<User> findAllInactiveUsers(@PathVariable String role){
		return service.findAllAdmins(role);
	}
	
	@DeleteMapping("/user/delete/{userId}")
	public ResponseEntity<ResponseStructure> deleteUser(@PathVariable Integer userId){
		return service.deleteUser(userId);
	}
	

}
