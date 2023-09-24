package com.jspiders.ombs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
	/** Here we are doing both data saving & sending mail for Account creation */
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO) throws MessagingException;
	
	/** This is for user login after signUp page using @RequestParam */
	public ResponseEntity<ResponseStructure<String>> userLogin(String userEmail, String userPassword);
	
	/** This is the valid method for User login using @RequestBody(not @RequestParam) & passing userEmail through cookie  */
	public ResponseEntity<ResponseStructure<String>> userLogin2(UserRequestDTO userRequestDTO, HttpServletResponse response);
	
	/** This method is to use cookie & perform some specific task(like update user name, etc)  */
	public ResponseEntity<ResponseStructure<String>> updateuserName(HttpServletRequest request, String name);
	
	/** 1st way: This delete operation is for soft deleting the data from database & deleted record should show as true
	 * (but in DB actually we are not deleting account, just changing isDeleted enum to True-> indicating that account is deleted) */
	public ResponseEntity<ResponseStructure<String>> enterIdToDeleteAccount(int userId);
	
	/** if user is found for deleting account using userId then call this method to confirm password to finally delete account 
	 * but above method has to give users userId or userEmail so that we can delete that particular userAccount only
	 * (but in DB actually we are not deleting account, just changing isDeleted enum to True-> indicating that account is deleted)  */
	public ResponseEntity<ResponseStructure<String>> enterPasswordToDelete(String password);
	
	/** 2nd way: to delete account */ 
	public ResponseEntity<ResponseStructure<String>> deleteUserAccount2(int id, String password);
	
	/** This is to send mail to user to the change password */
	public ResponseEntity<String> sendMailToChangePassword(String userEmail) throws MessagingException;
	
	/** Sending request to SAVE Newly Created Password to that sent Email object */
	public ResponseEntity<ResponseStructure<String>> createNewPassword(String userEmail,String newPassword);
	
//	----------- To Retrieve email -------------
	public ResponseEntity<String> retrieveEmail(String token);
	
//	---------- Product Entity operations ----------------------
	/** Save Product */
	public ResponseEntity<ResponseStructure<ProductResponseDTO>> saveProduct(HttpServletRequest request, ProductRequestDTO productRequestDTO);
	
	/** Update Product quantity */
	public ResponseEntity<ResponseStructure<String>> updateProductQuantity(HttpServletRequest request, String productName, int productQty);
	
	/** Delete Product */
	public ResponseEntity<ResponseStructure<String>> deleteProduct(HttpServletRequest request, String productName);
	
	/** Get List of Products */
	public ResponseEntity<ResponseStructure<List<ProductResponseDTO>>> getAllProducts();
	
	
	
	
}

