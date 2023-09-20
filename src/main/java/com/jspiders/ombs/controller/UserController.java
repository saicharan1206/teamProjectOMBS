package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.DeleteByIdRequest;
import com.jspiders.ombs.dto.ForgotPasswordEmail;
import com.jspiders.ombs.dto.ForgotPasswordEmailResponse;
import com.jspiders.ombs.dto.LoginResponse;
import com.jspiders.ombs.dto.LoginVerification;
import com.jspiders.ombs.dto.UpdateEmail;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.serviceimpl.UserServiceImpl;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/data")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserServiceImpl userServiceImpl;

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData (@RequestBody @Valid UserRequestDTO userRequestDTO)
	{
		return userService.saveData(userRequestDTO);
	}
	
	
	@PostMapping("/get/data")
	public ResponseEntity<ResponseStructure<LoginResponse>> loginVer (@RequestBody @Valid LoginVerification loginVerification)
	{
		return userService.loginVer(loginVerification);
	}
	
	@PostMapping("/post/mail/data")
	public ResponseEntity<ResponseStructure<ForgotPasswordEmailResponse>> forgotPassword (@RequestBody @Valid ForgotPasswordEmail forgotPasswordEmail)
	{
		return userServiceImpl.forgotPassword(forgotPasswordEmail);
	}
	
	@PostMapping("/delete/data")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteData (@RequestBody DeleteByIdRequest deleteByIdRequest)
	{
		return userService.deleteData(deleteByIdRequest);
	}
	
	@PostMapping("/update/data/email")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateEmail (@RequestBody UpdateEmail updateEmail)
	{
		return userService.updateEmail(updateEmail);
	}
	
	@GetMapping("/get/details")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> fetchAllDetailsById (@RequestBody DeleteByIdRequest deleteByIdRequest)
	{
		return userService.fetchAllDetailsById (deleteByIdRequest);
	}
	
}
