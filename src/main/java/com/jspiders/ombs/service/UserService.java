package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {	
	
	// To SAVE user:
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest) throws MessagingException;
	
	// To FIND user:(log in)
	public ResponseEntity<ResponseStructure<String>> logInUser(String email, String password);
	
	// Forgot Password
	public ResponseEntity<String> changePassword(String email) throws MessagingException;
	
	// Confirm PASSWORD from User
	public ResponseEntity<ResponseStructure<String>> confirmDeleteMyAccount(int id, String password);
	
	// to CREATE and CONFIRM Password
	public ResponseEntity<ResponseStructure<String>> confirmNewPassword(int userId,String newPasword);
	
	// to SAVE a new PRODUCT
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> addProduct(int userId, ProductRequestDTO prodRequest);
	
	// to DELETE a PRODUCT
	public ResponseEntity<ResponseStructure<String>> deleteProduct(int userId, int productId);
	
	// to UPDATE a PRODUCT
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> updateProduct(int id,ProductRequestDTO prodRequest);
	
	// to GET LIST of PRODUCTS
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> productList();
	
	
	
}
