package com.jspiders.ombs.serviceimpl;

import java.util.Date;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.UserRepo;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserAlreadyExistsException;
import com.jspiders.ombs.util.exception.UserNotFoundException;

import jakarta.mail.MessagingException;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepo userRepo;
	@Autowired
	UserRoleRepository userRoleRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequestDTO userRequestDTO) throws MessagingException {
		User save;
		User user=new User();
		
		UserRole userRole1; //mapping based on role
		
		if(userRepo.findByUserEmail(userRequestDTO.getUserEmail())==null){
			
			String role = userRequestDTO.getUserRole();
			System.out.println(role);
			
			UserRole userRole = new UserRole();
			userRole.setRole(role);
			
			user.setUserEmail(userRequestDTO.getUserEmail().toLowerCase());
			user.setUserpassword(userRequestDTO.getUserPassword());
			user.setUserrole(userRole);
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			
			userRole1 = userRoleRepository.save(userRole);

			save = userRepo.save(user);
			System.out.println(user.toString());
			userRole1.getRole();
			
			sendMimeMeassage(userRequestDTO.getUserEmail(), userRequestDTO.getUserRole());
			
		}
		else {
			throw new UserAlreadyExistsException("User already exists!!");
		}
			
		UserResponse responseDTO = new UserResponse();
		responseDTO.setUserId(save.getUserId());
		responseDTO.setUserFirstName(save.getUserFirstName());
		responseDTO.setUserLastName(save.getUserLastName());
		responseDTO.setUserRole(userRole1.getRole());
		responseDTO.setUserEmail(save.getUserEmail());
		responseDTO.setCreatedBy(save.getCreatedBy());
		responseDTO.setCreatedDate(save.getCreatedDate());
		responseDTO.setUpdatedBy(save.getUpdatedBy());
		responseDTO.setUpdatedDate(save.getUpdatedDate());
		
		ResponseStructure<UserResponse> responseStructure = new ResponseStructure<>();
		responseStructure.setData(responseDTO);
		responseStructure.setMessage("User registered successfully!!!");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		
		return new ResponseEntity<ResponseStructure<UserResponse>>(responseStructure, HttpStatus.CREATED);
	}

//	public void sendMimeMeassage(String userEmail, String role) throws MessagingException {
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setFrom("rakeshhadpad026@gmail.com");
//        helper.setTo(userEmail);
//        helper.setSubject("Account Created");
//        helper.setText("Your account has been created with role: " + role);
//
//        javaMailSender.send(message);
//        
//    }
	               //Sending Email while Signup
	    @Override
		public ResponseEntity<String> sendMimeMeassage(String userEmail, String role) throws MessagingException{
			
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime, true);
			
			message.setFrom("rakeshhadpad026@gmail.com");
			message.setTo(userEmail);
			message.setSubject("Account Created");
			message.setText("Your account has been created with role: " + role);
			 javaMailSender.send(mime);
			 return new ResponseEntity<String>("Mime messages sent successfully", HttpStatus.OK);
		}
		
           //login
		@Override
		public ResponseEntity<ResponseStructure<UserResponse>> getUser(String email, String password)
				throws UserNotFoundException {
			User user = userRepo.findByUserEmail(email);
			
			if (user != null && user.getUserpassword().equals(password)) {
				UserResponse response = new UserResponse();
				response.setUserId(user.getUserId());
				response.setUserFirstName(user.getUserFirstName());
				response.setUserLastName(user.getUserLastName());
				response.setCreatedBy(user.getCreatedBy());
				response.setCreatedDate(user.getCreatedDate());
				response.setUserEmail(user.getUserEmail());
				response.setUserRole(user.getUserrole().getRole());
				response.setUpdatedDate(user.getUpdatedDate());
				response.setUpdatedBy(user.getUpdatedBy());
				response.setUserEmail(user.getUserEmail());
	
				ResponseStructure<UserResponse> responseStructure = new ResponseStructure<>();
				responseStructure.setData(response);
				responseStructure.setMessage("Login succesfull");
				responseStructure.setStatusCode(HttpStatus.FOUND.value());
	
				return new ResponseEntity<ResponseStructure<UserResponse>>(responseStructure, HttpStatus.FOUND);
			}
			
			throw new UserNotFoundException("user not found");
		}
		}
	




