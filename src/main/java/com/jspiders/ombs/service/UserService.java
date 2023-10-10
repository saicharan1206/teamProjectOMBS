package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.LoginRequestDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.util.ResponseStructure;





public interface UserService {

	public ResponseEntity<ResponseStructure> addUser(UserRequestDTO dto) ;
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> loginUser(LoginRequestDTO loginRequestDTO);
	
// mail pwd:gjth ejqh mrti vjnh
	public ResponseEntity<ResponseStructure<String>> signupmail(String toMail ,String userName,String role) ;
	
	public String  generateResetToken();
	public String createPwdResetLink(String resetToken) ;

	ResponseEntity<ResponseStructure> sendForgotPwdLink(String toMail, String resetToken);
	public ResponseEntity<ResponseStructure<String>> resetPassword(String email,String newPassword,String confirmPwd) ;
	public ResponseEntity<ResponseStructure<User>> deleteUser(Integer userid);
}
