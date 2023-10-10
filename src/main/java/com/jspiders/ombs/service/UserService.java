package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.LoginRequestDTO;
import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.entity.Products;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;


public interface UserService {

	public ResponseEntity<ResponseStructure> createUser(UserRequestDTO userDTO) throws MessagingException;
	
	public ResponseEntity<ResponseStructure> userLogin(LoginRequestDTO loginRequestDTO);

	public ResponseEntity<ResponseStructure> sendMail(String to, String username, String role);
	
	public String generateResetToken();
	
	public String createPasswordResetLink(String resetToken);
	
	public ResponseEntity<ResponseStructure> sendPasswordLink(String to, String resetToken);
	
	public ResponseEntity<ResponseStructure> resetPassword(String email, String password,String confirmPwd);
	
	public ResponseEntity<ResponseStructure> findUserById(Integer userId);
	
	public ResponseEntity<ResponseStructure> deleteUser(Integer userId);

	public List<User> findAllAdmins(String role);
	
	//public ResponseEntity<ResponseStructure> addProduct(ProductRequestDTO productRequestDTO);

	
	//public ResponseEntity<ResponseStructure> findProduct();
}
