package com.jspiders.ombs.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.jspiders.ombs.enums.IsDeleted;
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
		
		
		 @Override
		  public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(String email) throws UserNotFoundException {
		      User user = userRepo.findByUserEmail(email);

		      if (user != null && user.getUserrole().getRole().equalsIgnoreCase("Admin")) {
		        
		          user.setIsDeleted(IsDeleted.TRUE);
		          userRepo.save(user);

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
		          responseStructure.setMessage("User is deleted successfully");
		          responseStructure.setStatusCode(HttpStatus.OK.value());

		          return new ResponseEntity<>(responseStructure, HttpStatus.OK);
		      } else {
		          throw new UserNotFoundException("User is not authorized to delete");
		      }
		  }

		  @Override
		  public ResponseEntity<ResponseStructure<UserResponse>> updateUser(String email, UserRequestDTO userRequestDTO) throws UserNotFoundException {
		      User user = userRepo.findByUserEmail(email);

		      if (user != null) {
		          String newEmail = userRequestDTO.getUserEmail().toLowerCase();
	         
		          if (!email.equalsIgnoreCase(newEmail) && userRepo.findByUserEmail(newEmail) != null) {
		              throw new UserAlreadyExistsException("Email address is already in use.");
		          }

		          user.setUserEmail(newEmail);
		          user.setUserpassword(userRequestDTO.getUserPassword());
		          user.setUserFirstName(userRequestDTO.getUserFirstName());
		          user.setUserLastName(userRequestDTO.getUserLastName());
		          user.getUserrole().setRole(userRequestDTO.getUserRole()); 

		          User updatedUser = userRepo.save(user);

		          UserResponse responseDTO = new UserResponse();
		          responseDTO.setUserId(updatedUser.getUserId());
		          responseDTO.setUserFirstName(updatedUser.getUserFirstName());
		          responseDTO.setUserLastName(updatedUser.getUserLastName());
		          responseDTO.setUserRole(updatedUser.getUserrole().getRole());
		          responseDTO.setUserEmail(updatedUser.getUserEmail());
		          responseDTO.setCreatedBy(updatedUser.getCreatedBy());
		          responseDTO.setCreatedDate(updatedUser.getCreatedDate());
		          responseDTO.setUpdatedBy(updatedUser.getUpdatedBy());
		          responseDTO.setUpdatedDate(updatedUser.getUpdatedDate());

		          ResponseStructure<UserResponse> responseStructure = new ResponseStructure<>();
		          responseStructure.setData(responseDTO);
		          responseStructure.setMessage("User updated successfully!");
		          responseStructure.setStatusCode(HttpStatus.OK.value());

		          return ResponseEntity.ok(responseStructure);
		      } else {
		          throw new UserNotFoundException("User not found");
		      }
		  }
		  

		  @Override
		  public ResponseEntity<ResponseStructure<List<UserResponse>>> getAllUsers() {
		      List<User> allUsers = userRepo.findAll();
		      List<UserResponse> adminUserResponseDTOs = new ArrayList<>();

		      for (User user : allUsers) {
		          if ("Admin".equalsIgnoreCase(user.getUserrole().getRole())) {
		              UserResponse responseDTO = new UserResponse();
		              responseDTO.setUserId(user.getUserId());
		              responseDTO.setUserFirstName(user.getUserFirstName());
		              responseDTO.setUserLastName(user.getUserLastName());
		              responseDTO.setUserEmail(user.getUserEmail());
		              responseDTO.setUserRole(user.getUserrole().getRole());
		              responseDTO.setCreatedBy(user.getCreatedBy());
			          responseDTO.setCreatedDate(user.getCreatedDate());
			          responseDTO.setUpdatedBy(user.getUpdatedBy());
			          responseDTO.setUpdatedDate(user.getUpdatedDate());

		              adminUserResponseDTOs.add(responseDTO);
		          }
		      }

		      ResponseStructure<List<UserResponse>> responseStructure = new ResponseStructure<>();
		      responseStructure.setData(adminUserResponseDTOs);
		      responseStructure.setMessage("All admin users retrieved successfully");
		      responseStructure.setStatusCode(HttpStatus.OK.value());

		      return ResponseEntity.ok(responseStructure);
		  }
		
		}
	
