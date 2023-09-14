package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

@RestController
@Validated
@RequestMapping("/users")
@CrossOrigin
public class UserController {
	@Autowired
	private UserService service;

	@PostMapping
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody UserRequestDTO userRequestDTO) throws MessagingException {
		return service.saveUser(userRequestDTO);
	}
	
	@GetMapping("/login")
	public ResponseEntity<ResponseStructure<String>> loginUser(@RequestParam String emailaddress,@RequestParam String password)
	{
		return service.loginUser(emailaddress, password);
	}
	@PostMapping("/mail")
	public ResponseEntity<String> sendMail(@RequestBody MessageData messageData)
	{
		return service.sendMail(messageData);
	}
	
	@PostMapping("/mimemessage/send")
	public ResponseEntity<String> myMail(@RequestBody MessageData messageData)throws MessagingException
	{
		return service.myMail(messageData);
	}
}
