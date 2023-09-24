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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@CrossOrigin
	@PostMapping("/users")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(@RequestBody @Valid UserRequestDTO userRequestDTO) throws MessagingException{
		return userService.saveUser(userRequestDTO);
	}
	
	@CrossOrigin
//	@PostMapping("/login") // for UI
	@PostMapping("/users/login1")
	public ResponseEntity<ResponseStructure<String>> login1(@RequestParam String email, String password){
		return userService.userLogin(email, password);
	}
	
	@PostMapping("/users/login2")
	public ResponseEntity<ResponseStructure<String>> login2(@RequestBody UserRequestDTO userRequestDTO, HttpServletResponse response){
		return userService.userLogin2(userRequestDTO, response);
	}
	
	@GetMapping("/users/getcookie")
	public ResponseEntity<ResponseStructure<String>> updateuserName(HttpServletRequest request, @RequestParam String name){
		return userService.updateuserName(request, name);
	}
	
	@CrossOrigin
	@PostMapping("/users/{userEmail}/sendMail")
	public ResponseEntity<String> sendMailToChangePwd(@PathVariable String userEmail) throws MessagingException{
		return userService.sendMailToChangePassword(userEmail);
	}
	
	@DeleteMapping("/users/{userId}/delete1")
	public ResponseEntity<ResponseStructure<String>> idToDeleteAccount(@PathVariable int userId){
		return userService.enterIdToDeleteAccount(userId);
	}
	
	@DeleteMapping("/users/{userPassword}/delete1")
	public ResponseEntity<ResponseStructure<String>> passwordToDeleteAccount(@RequestParam String userPassword){
		return userService.enterPasswordToDelete(userPassword);
	}
	
	@DeleteMapping("/users/delete2")
	public ResponseEntity<ResponseStructure<String>> deleteAccount2(@RequestParam int userId, String userPassword){
		return userService.deleteUserAccount2(userId, userPassword);
	}
	
	@PutMapping("/users/resetPassword")
	public ResponseEntity<ResponseStructure<String>> createNewPassword(@RequestParam String userEmail,String newPassword){
		return userService.createNewPassword(userEmail,newPassword);
	}
	
//	-------------------
	@GetMapping("/users/retrieve")
    public ResponseEntity<String> retrieveEmail(@RequestParam String token) {
		return userService.retrieveEmail(token);
	}
	
//	---------- Product Entity operations ----------------------
	@PostMapping("/users/userEmail/products")
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(HttpServletRequest request,@RequestBody ProductRequestDTO productRequestDTO){
		return userService.saveProduct(request,productRequestDTO);
	}
	
	@PutMapping("/users/userEmail/products/productName/update")
	public ResponseEntity<ResponseStructure<String>> updateProductQuantity(HttpServletRequest request, @RequestParam String productName, int productQty) {
		return userService.updateProductQuantity(request, productName, productQty);
	}
	
	@DeleteMapping("/users/userEmail/products/{productName}/delete")
	public ResponseEntity<ResponseStructure<String>> deleteProduct(HttpServletRequest request,@PathVariable String productName){
		return userService.deleteProduct(request, productName);
	}
	
	@GetMapping("/products")
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> getAllProducts(){
		return userService.getAllProducts();
	}
	
}










