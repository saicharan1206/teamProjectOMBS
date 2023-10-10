package com.jspiders.ombs.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.LoginRequestDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.ISDELETED;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.exception.CannotUseSamePasswordException;
import com.jspiders.ombs.exception.UserWithSameEmailExist;
import com.jspiders.ombs.repository.UserRepo;
import com.jspiders.ombs.repository.UserRoleRepo;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailNotFoundException;
import com.jspiders.ombs.util.exception.ForbiddenOperationException;
import com.jspiders.ombs.util.exception.PasswordMissMatchException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private UserRoleRepo roleRepo;
	@Autowired
	private JavaMailSender javaMailSender;
	boolean flag;
	@Override
	public ResponseEntity<ResponseStructure> addUser(UserRequestDTO userRequestDTO) {
		User user = new User();
		UserRole role = userRepo.fetchRole(userRequestDTO.getUserRole());
		
		if (role==null) {
			UserRole userRole = new UserRole();
			userRole.setRole(userRequestDTO.getUserRole());
			role=userRole;
			roleRepo.save(role);
		}
		user.setUserFirstName(userRequestDTO.getUserFirstName());
		user.setUserLastName(userRequestDTO.getUserLastName());
		user.setEmailId(userRequestDTO.getEmailId().toLowerCase());
		user.setPassword(userRequestDTO.getPassword());
			user.setUserRole(role);
			user.setDeleteStatus(ISDELETED.FALSE);
			
           List<User> users = userRepo.findAll();
           for (User user2 : users) {
			if (user2.getEmailId().equals(user.getEmailId())) {
			   throw new UserWithSameEmailExist("Email id already in database");
			}
		}
		User user2 = userRepo.save(user);
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		userResponseDTO.setUserId(user2.getUserId());
		userResponseDTO.setUserFirstName(user2.getUserFirstName());
		userResponseDTO.setUserLastName(user2.getUserLastName());
		userResponseDTO.setEmailId(user2.getEmailId());
		
		
		ResponseStructure<UserResponseDTO>responseStructure = new ResponseStructure<UserResponseDTO>();
		responseStructure.setData(userResponseDTO);
		responseStructure.setMessage("user data added successfully");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
//		signupmail(userRequestDTO.getEmailId(), userRequestDTO.getUserFirstName(), userRequestDTO.getUserRole());
		return  new ResponseEntity<ResponseStructure>(responseStructure,HttpStatus.CREATED);
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> loginUser(LoginRequestDTO loginRequestDTO) {
		     User user = userRepo.findByEmailId(loginRequestDTO.getEmailId());
		     
		     if (user==null) {
				throw new EmailNotFoundException("Email not Found in DB");
			}
		     else if (!user.getPassword().equals(loginRequestDTO.getPassword())) {
				throw  new PasswordMissMatchException("Password is Not Matching");
			}
		     UserResponseDTO responseDTO = new UserResponseDTO();
		     responseDTO.setEmailId(user.getEmailId());
		     responseDTO.setUserFirstName(user.getUserFirstName());
		     responseDTO.setUserId(user.getUserId());
		     responseDTO.setUserLastName(user.getUserLastName());
		     
		     ResponseStructure<UserResponseDTO>structure = new ResponseStructure<UserResponseDTO>();
		     structure.setData(responseDTO);
		     structure.setMessage("Login Successful");
		     structure.setStatusCode(HttpStatus.OK.value());
		     
		return  new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.OK);
	}
	@Override
	public ResponseEntity<ResponseStructure<String>> signupmail(String toMail, String userName, String role) {
		SimpleMailMessage mailMessage = new  SimpleMailMessage();
		mailMessage.setTo(toMail);
		mailMessage.setSubject("Welcome");
		mailMessage.setText("Dear "+userName+" your account successfully created as "+role);
		javaMailSender.send(mailMessage);
		ResponseStructure<String> responseStructure= new ResponseStructure<String>();
		responseStructure.setData(mailMessage.getText());
		responseStructure.setMessage(mailMessage.getText());
		responseStructure.setStatusCode(HttpStatus.OK.value());
		return  new ResponseEntity<ResponseStructure<String>>(responseStructure,HttpStatus.OK);
	}
	@Override
	public ResponseEntity<ResponseStructure> sendForgotPwdLink(String toMail,String resetToken) {
		if (userRepo.findByEmailId(toMail)==null) {
			throw new  EmailNotFoundException("Email not found in Database");
		}
		SimpleMailMessage mailMessage= new  SimpleMailMessage();
		mailMessage.setTo(toMail);
		mailMessage.setSubject("Link to Reset Password");
		mailMessage.setText("to Reset the password click the below link "+createPwdResetLink(resetToken)+toMail);
		
		javaMailSender.send(mailMessage);
		ResponseStructure<String> responseStructure= new ResponseStructure<String>();
		responseStructure.setData("Mail sent to reset password");
		responseStructure.setMessage("Mail sent to reset password");
		responseStructure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure>(responseStructure,HttpStatus.OK);
	}
	@Override
	public String generateResetToken() {
		String resetToken=UUID.randomUUID().toString();
		return resetToken;
	}
	@Override
	public String createPwdResetLink(String resetToken) {
		// TODO Auto-generated method stub
		return "http://localhost:3000/reset/";
	}
	@Override
	public ResponseEntity<ResponseStructure<String>> resetPassword(String emailId, String newPassword,String confirmPwd) {
	User user = userRepo.findByEmailId(emailId);
	if (!newPassword.equals(confirmPwd)) {
		throw new PasswordMissMatchException("password should match with confirm password");
	}
	if (user.getPassword().equalsIgnoreCase(newPassword)) {
		throw new CannotUseSamePasswordException("you are trying to reset with same password ");
	}
	user.setPassword(newPassword);
	userRepo.save(user);
	ResponseStructure<String>responseStructure= new ResponseStructure<String>();
	responseStructure.setData("Password reset successfully");
	responseStructure.setMessage("Dear "+user.getUserFirstName()+" your password reset successfully");
	responseStructure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<String>>(responseStructure,HttpStatus.OK);
	}
	@Override
	public ResponseEntity<ResponseStructure<User>> deleteUser(Integer userid) {
		Optional<User> optional = userRepo.findById(userid);
		if (optional.isPresent()) {
			User user = optional.get();
			if (user.getUserRole().getRole().equals("USER")) {
				user.setDeleteStatus(ISDELETED.TRUE);
				User user2 = userRepo.save(user);
				ResponseStructure<User>responseStructure=new ResponseStructure<User>();
				responseStructure.setData(user2);
				responseStructure.setMessage("User deleted successfully");
				responseStructure.setStatusCode(HttpStatus.OK.value());
				return new ResponseEntity<ResponseStructure<User>>(responseStructure,HttpStatus.OK);
			}
			else {
				throw new ForbiddenOperationException("admin cannot delete another admin");
			}
			
		}
		else {
			throw new UserNotFoundByIdException("User not found by this mail");
		}
		
		
	}
	
}
