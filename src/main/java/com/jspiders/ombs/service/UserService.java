package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;
import com.jspiders.ombs.dto.DeleteByIdRequest;
import com.jspiders.ombs.dto.ForgotPasswordEmail;
import com.jspiders.ombs.dto.ForgotPasswordEmailResponse;
import com.jspiders.ombs.dto.LoginResponse;
import com.jspiders.ombs.dto.LoginVerification;
import com.jspiders.ombs.dto.PasswordResetRequest;
import com.jspiders.ombs.dto.UpdateEmail;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface UserService {
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData (UserRequestDTO userRequestDTO);
	
	public ResponseEntity<ResponseStructure<LoginResponse>> loginVer (LoginVerification loginVerification);
	
	public ResponseEntity<ResponseStructure<ForgotPasswordEmailResponse>> forgotPassword (ForgotPasswordEmail forgotPasswordEmail) throws MessagingException;
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteData (DeleteByIdRequest deleteByIdRequest);
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateEmail (UpdateEmail updateEmail);
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> fetchAllDetailsById (DeleteByIdRequest deleteByIdRequest);
	
	public ResponseEntity<ResponseStructure<ForgotPasswordEmailResponse>> updatePassword (PasswordResetRequest passwordResetRequest);
	
}
