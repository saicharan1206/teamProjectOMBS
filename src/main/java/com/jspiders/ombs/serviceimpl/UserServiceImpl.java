package com.jspiders.ombs.serviceimpl;

import java.util.Date;
import java.util.List;

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
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepositary;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailException;
import com.jspiders.ombs.util.exception.EmailNotFoundException;
import com.jspiders.ombs.util.exception.PasswordNotMatchingExceeption;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserRoleRepositary roleRepo;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	
//	UserServiceImpl  service = new UserServiceImpl();
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequestDTO userRequest) throws MessagingException {
		User user1;
		User user = new User();

		if (userRepo.findByUserEmail(userRequest.getUserEmail()) == null) {
			UserRole userRole = roleRepo.getUserRoleByRole(userRequest.getUserRole());
			System.out.println(userRole);
			if (userRole == null) {
				userRole = new UserRole();
				userRole.setUserRole(userRequest.getUserRole());
				roleRepo.save(userRole);
			}
			user.setUserEmail(userRequest.getUserEmail().toLowerCase());
			user.setUserPassord(userRequest.getUserPassord());
			user.setUserFirstName(userRequest.getUserFirstName());
			user.setUserLastName(userRequest.getUserLastName());
			user.setRole(userRole);
			userRepo.save(user);
			
			/// for sending a mail
			// ----------------------------------------------------
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime, false);

			message.setTo(userRequest.getUserEmail());
			message.setSubject("Registeration response");

			String emailBody = "Hello "+userRequest.getUserFirstName()+" "+userRequest.getUserLastName()+" is registerd with our App"
					+"<br><br><h4>Thanks & Regards <br>"
					+"Preetham h P <br>"
					+"Banglore<h4>"
					+"<img src=\"https://jspiders.com/_nuxt/img/logo_jspiders.3b552d0.png\" width=\"250\">";
			message.setText(emailBody,true);
			message.setSentDate(new Date());
			javaMailSender.send(mime);
			
			
			//--------------------------------------------------------
			
		} else {
			throw new EmailException("email is already Present");
		}

		UserResponse response = new UserResponse();
		response.setUserId(user.getUserId());
		response.setUserEmail(user.getUserEmail());
		ResponseStructure<UserResponse> structure = new ResponseStructure<UserResponse>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("User Saved Succssefully");
		structure.setData(response);
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> userlogin(UserRequestDTO userRequest) {
		User user = null;
		 user = userRepo.findByUserEmail(userRequest.getUserEmail());
		if (user != null) {
			if (user.getUserPassord().equals(userRequest.getUserPassord())) {
				
				UserResponse response = new UserResponse();
				response.setUserId(user.getUserId());
				response.setUserFirstName(user.getUserFirstName());
				response.setUserLastName(user.getUserLastName());
				response.setRole(user.getRole());
				ResponseStructure<UserResponse> structure = new ResponseStructure<UserResponse>();
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Loign succssfully");
				structure.setData(response);
				return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.FOUND);
				
			} else {
				throw new PasswordNotMatchingExceeption("Password is not matching");
			}
		} else {
			throw new EmailNotFoundException("Email is not found");
		}
	}

	@Override
	public ResponseEntity<String> changePassword(String email) throws MessagingException {
		
		User user = userRepo.findByUserEmail(email);
		
		if(user!= null) {
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime, false);

			message.setTo(user.getUserEmail());
			message.setSubject("Change Password Link");

			String emailBody = "Hello "+user.getUserFirstName()+" "+user.getUserLastName()+" you want to change the password Using below Link"
					+"<a href=\"\"></a>"
					+"<br><br><h4>Thanks & Regards <br>"
					+"Preetham h P <br>"
					+"Banglore<h4>"
					+"<img src=\"https://jspiders.com/_nuxt/img/logo_jspiders.3b552d0.png\" width=\"250\">";
			message.setText(emailBody,true);
			message.setSentDate(new Date());
			javaMailSender.send(mime);
			return new ResponseEntity<String>("Email sent Sucssfully", HttpStatus.OK);
		}
		else {
			throw new EmailNotFoundException("Email is not Present");
		}
	
		
	
	}


}

//chyc kfut scfk rarn
