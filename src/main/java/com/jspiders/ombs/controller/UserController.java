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

import com.jspiders.ombs.dto.ForgotRequest;
import com.jspiders.ombs.dto.LoginRequest;
import com.jspiders.ombs.dto.LoginResponse;
import com.jspiders.ombs.dto.ProductRequest;
import com.jspiders.ombs.dto.ProductResponse;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService service;
	
	@CrossOrigin
	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData(@RequestBody @Valid UserRequestDTO user)
	{
		return service.saveData(user);
	}
	
	@CrossOrigin
	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<LoginResponse>> loginUser(@RequestBody LoginRequest login)
	{
		return service.loginUser(login);
	}
	
	@CrossOrigin
	@PostMapping("/forgotpassword")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotRequest forgot) throws MessagingException
	{
		return service.forgotPassword(forgot);
	}
	
	@PutMapping("/updateUser")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(@RequestBody UserRequestDTO user, @RequestParam int userId)
	{
		return service.updateUser(user, userId);
	}
	
	@DeleteMapping("/deleteUser")
	public ResponseEntity<ResponseStructure<LoginResponse>> deleteAccount(@RequestParam int userId)
	{
		return service.deleteAccount(userId);
	}
	
	@CrossOrigin
	@PostMapping("/resetPassword")
	public ResponseEntity<ResponseStructure<String>> resetPassword(@RequestParam String userEmail, @RequestParam String newPassword, @RequestParam String confirmPassword)
	{
		return service.resetPassword(userEmail, newPassword, confirmPassword);
	}
	
	@CrossOrigin("*")
	@PostMapping("/products")
	public ResponseEntity<ResponseStructure<ProductResponse>> saveProduct(@RequestBody ProductRequest product)
	{
		return service.saveProduct(product);
	}
	
    @CrossOrigin()
	@GetMapping("/allproducts")
	public ResponseEntity<ResponseStructure<List<ProductResponse>>> findAllProducts()
	{
		return service.findAllProducts();
	}
    
    @CrossOrigin("*")
    @PutMapping("/updateproduct/{productId}")
    public ResponseEntity<ResponseStructure<ProductResponse>> updateProduct(@RequestBody ProductRequest product,@PathVariable int productId)
    {
    	return service.updateProduct(product, productId);
    }
    
    @CrossOrigin("*")
    @DeleteMapping("/deleteproduct/{productId}")
    public ResponseEntity<ResponseStructure<ProductResponse>> deleteStudent(@PathVariable int productId)
    {
    	return service.deleteProduct(productId);
    }
    
     @CrossOrigin("*")
    @GetMapping("/findById/{productId}")
    public ResponseEntity<ResponseStructure<ProductResponse>> findByIdProduct(@PathVariable int productId)
    {
    	return service.findByIdProduct(productId);
    }
	
}
