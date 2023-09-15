package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {
	/** Here we are doing both data saving & sending mail for Account creation */
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO) throws MessagingException;
	
	/** This is for login page after signUp page */
	public ResponseEntity<ResponseStructure<String>> userLogin(String userEmail, String userPassword);
	
	/** we can do login using another UserRequestDTO also */
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin2(UserRequestDTO userRequestDTO);
	
	/** This is to send mail to change password */
	public ResponseEntity<String> changePassword(String email) throws MessagingException;
	
	/** This delete operation is for soft deleting the data from database & deleted record should show as true */
	public ResponseEntity<ResponseStructure<String>> enterIdToDeleteAccount(int userId);
	
	/** if user is found for deleting account using userId then call this method to confirm password to finally delete account
	 * (but in DB actually we are not deleting account, just changing isDeleted enum to True-> indicating that account is deleted  */
	public ResponseEntity<ResponseStructure<String>> enterPasswordToDelete(String password);
	
	/** second way to delete account */ 
	public ResponseEntity<ResponseStructure<String>> deleteUserAccount2(int id, String password);
	
}

