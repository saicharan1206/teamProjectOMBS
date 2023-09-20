package com.jspiders.ombs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.ForgotEmailResponse;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	private UserService service;

	
	@PostMapping("/saveuser")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody UserRequestDTO request) {
		return service.saveUser(request);
	}

	
	@GetMapping("/getuser/{email}/{password}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(@PathVariable String email,
			@PathVariable String password) {
		return service.getUser(email, password);
	}
	
	@GetMapping("/sendemail/{email}")
	public ResponseEntity<ResponseStructure<ForgotEmailResponse>> sendforgotemail(@PathVariable String email) throws MessagingException{
		return service.sendforgotemail(email);
	}
	
	@DeleteMapping("/delete/{email}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(@PathVariable String email){
		return service.deleteUser(email);
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUSer(@RequestBody UserRequestDTO request){
		return service.updateUser(request);
	}
	
	@GetMapping("/findall")
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findall(){
		return service.findall();
	}
	
	@PatchMapping("/updatepassword/{password}/{confirmpassword}/{email}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updatePassword(@PathVariable String password, @PathVariable String confirmpassword, @PathVariable String email ){
		return service.updatePassword(password,confirmpassword,email);
	}
	
	
	
	
}
