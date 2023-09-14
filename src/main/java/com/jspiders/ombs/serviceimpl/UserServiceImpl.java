package com.jspiders.ombs.serviceimpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.Role;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyFoundException;
import com.jspiders.ombs.util.exception.UserNotFoundByEmailException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserRoleRepository roleRepo;
	
	@Autowired
	private JavaMailSender javaMailSender; 
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest) throws MessagingException{
		
		String email = userRequest.getUserEmail().toLowerCase();
		String roleName = userRequest.getUserRole().toLowerCase();
		
		// User Entity
		User object = userRepo.findByUserEmail(email);
		
		Role userRole = new Role();
		
		if(object==null){
		
			// Role Entity
			userRole = roleRepo.findByRoleName(roleName); // Sales	
			
			if(userRole == null) {
				userRole = new Role();
				userRole.setRoleName(roleName);
				roleRepo.save(userRole);
			}
				
				User user = new User();
				user.setUserFirstName(userRequest.getUserFirstName());
				user.setUserLastName(userRequest.getUserLastName());
				user.setUserRole(userRole);
				user.setUserEmail(email);
				user.setUserPassword(userRequest.getUserPassword());	
				
		 /** Because of the EnableJpaAuditing & @EntityListeners(AuditingEntityListener.class) **/
				/** Created Date -- user.setCreatedDate(LocalDateTime.now());**/
				/** Updated Date -- user.setUpdatedDate(LocalDateTime.now());**/
				// UpdatedBy
				
//				Role role = roleRepo.save(userRole);
				User user2 = userRepo.save(user);
							
				UserResponseDTO response = new UserResponseDTO();
				response.setUserId(user2.getUserId());
				response.setUserFirstName(user2.getUserFirstName());
				response.getUserRole(userRole.getRoleName());
				
				ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
				structure.setStatusCode(HttpStatus.CREATED.value());
				structure.setMessage("User Data inserted Successfully");
				structure.setData(response);
				
		
		/******* MAIL : ACCOUNT CREATED SUCCESSFULLY *********/
				
				MimeMessage mime = javaMailSender.createMimeMessage();
				MimeMessageHelper message = new MimeMessageHelper(mime, true);
				
				message.setTo(userRequest.getUserEmail());
				message.setSubject("Account Creation Response");

				String emailBody = "Hi "+user2.getUserFirstName()+", your account has been created successfully."					 
									
						+"<br><h4>Thanks & Regards</h4>"
						+"<h4>Admin Jspiders</h4>" 
						+"<h4>Jspiders Basavanagudi, Bangalore</h4>"
						+"<img src=\"https://media.istockphoto.com/id/1365830421/vector/hands-holding-house-symbol-with-heart-shape-thick-line-icon-with-pointed-corners-and-edges.jpg?s=1024x1024&w=is&k=20&c=SUp17dtO-N7qhENwnqxxEYD3SIFwfcu5-e9RCp4vlLw=\" width=\"120\">";

				message.setText(emailBody , true); 
				message.setSentDate(new Date());
				
				javaMailSender.send(mime);
				
				return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
			}
		throw new EmailAlreadyFoundException("This Email is not applicable");	
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> logInUser(String email, String password) {

		User user = userRepo.findByUserEmail(email.toLowerCase());		
		ResponseStructure<String> structure = new ResponseStructure<String>();
		
		if(user != null) {
			
			/** If user email is Correct **/
			System.out.println(email + " " + password);
			System.out.println(user.getUserEmail() + " " + user.getUserPassword());
			
			String pword = user.getUserPassword();
			
			if(password.equals(pword)) {
				
				/** password == pword (Compares address).. 
				 * 	password.equals(pword) (Compares Values)
				 */
				
				/** If user password is Correct **/
				System.out.println("Password is Correct");
				
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Successfully !!");
				structure.setData("Welcome Home, " +user.getUserRole().getRoleName());
				
				return new ResponseEntity<ResponseStructure<String>>(structure , HttpStatus.FOUND);
			}
			
			/** If user password is inCorrect **/
			System.out.println("Password is InCorrect");
			
			structure.setStatusCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Sorry!! Please Enter Valid Password");
			structure.setData("No User Found for this Credentials");
			
			return new ResponseEntity<ResponseStructure<String>>(structure , HttpStatus.NOT_FOUND);
		}
		/** If user email is inCorrect **/
		throw new UserNotFoundByEmailException("Invalid Email !!");
	}

	
	@Override
	public ResponseEntity<String> changePassword(String email) throws MessagingException {
		
		User user = userRepo.findByUserEmail(email);
		
		if(user != null) {
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime, true);
			
			message.setTo(email);
			message.setSubject("Link to change password");
			
			String emailBody = "Hi "+user.getUserFirstName()+", you can change the password using this below link."					 
					
							+"<br><h4>Thanks & Regards</h4>"
							+"<h4>Admin Jspiders</h4>" 
							+"<h4>Jspiders Basavanagudi, Bangalore</h4>"
							+"<img src=\"https://media.istockphoto.com/id/1365830421/vector/hands-holding-house-symbol-with-heart-shape-thick-line-icon-with-pointed-corners-and-edges.jpg?s=1024x1024&w=is&k=20&c=SUp17dtO-N7qhENwnqxxEYD3SIFwfcu5-e9RCp4vlLw=\" width=\"120\">";
			
			message.setText(emailBody, true);
			message.setSentDate(new Date());
			
			javaMailSender.send(mime);

			return new ResponseEntity<String> ("Mail has been sent, check your mail" , HttpStatus.OK);
		}
		
		throw new UserNotFoundByEmailException("Please, Enter Valid Email");
	}
}
