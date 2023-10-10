package com.jspiders.ombs.serviceimpl;
//djbi kygv xngo scjn - app password
/*** 
1. This is Service Implementation class which implements service interface
2. private UserRepo variable is created and autowired
3. Annotations
	@Service - It is a stereotype annotations in Spring that help with 
				component scanning and bean registration.
			   It marks a class as a Spring-managed service or component
 	@Autowired - When you annotate a field, constructor, or method 
 				parameter with @Autowired, Spring will automatically 
 				search for a bean of the same type (or compatible type) 
 				to inject into that field, constructor, or parameter.
4. There is a method by name createUser() which accepts UserRequestDTO as an input
	parameter.
5. Created the User Object and read email and password through UserRequestDTO
	and set them in User entity class and convert the chars in email to lowercase
6. repo.findAll() is used to find the data present in the database. it 
	returns List of User entity
7. Iterate the List using forEach compare the emails present in List and 
   the emails entered by user, if both are same throw the custom 
   emailIdAlreadyPresentException with a proper message.
8. if both the emails are different then save the data into database 
	using repo.save(user) it accepts user object as input. It 
	
***/

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.LoginRequestDTO;
import com.jspiders.ombs.dto.MessageData;
import com.jspiders.ombs.dto.ProductRequestDTO;
import com.jspiders.ombs.dto.ProductResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.ISDELETED;
import com.jspiders.ombs.entity.Products;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.ProductsRepo;
import com.jspiders.ombs.repository.UserRepo;
import com.jspiders.ombs.repository.UserRoleRepo;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailNotFoundException;
import com.jspiders.ombs.util.exception.ForbiddenOperationException;
import com.jspiders.ombs.util.exception.NotAuthorizedToAddProductException;
import com.jspiders.ombs.util.exception.PasswordMismatchException;
import com.jspiders.ombs.util.exception.ProductAlreadyPresentException;
import com.jspiders.ombs.util.exception.UserCannotBeDeletedException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;
import com.jspiders.ombs.util.exception.CannotUseSamePasswordException;
import com.jspiders.ombs.util.exception.emailIdAlreadyPresentException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;



@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo repo;

	@Autowired
	private UserRoleRepo roleRepo;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private ProductsRepo productsRepo;

	@Override
	public ResponseEntity<ResponseStructure> createUser(UserRequestDTO userDTO) {
			
			User user =new User();
			UserRole role = repo.fetchRole(userDTO.getUserRole());
			
			if(role==null) {
				UserRole userRole= new UserRole();
				userRole.setRole(userDTO.getUserRole());
				role=userRole;
				roleRepo.save(role);
			}
			
			user.setUserFirstName(userDTO.getUserFirstName());
			user.setUserLastName(userDTO.getUserLastName());
			user.setEmail(userDTO.getEmail().toLowerCase());
			user.setPassword(userDTO.getPassword());
			user.setUserRole(role);
			user.setDeleteStatus(ISDELETED.FALSE);
	
			List<User> findAllUsers = repo.findAll();
		
		for (User user2 : findAllUsers) {
			if(user2.getEmail().equals(user.getEmail())) {
				throw new emailIdAlreadyPresentException("Email is already present in database");
			}
		}
		
			User saveUser = repo.save(user);
			UserResponseDTO userResponse = new UserResponseDTO();
			userResponse.setUserId(saveUser.getUserId());
			userResponse.setUserFirstName(saveUser.getUserFirstName());
			userResponse.setUserLastName(saveUser.getUserLastName());
			userResponse.setEmail(saveUser.getEmail());
			userResponse.setUserRole(saveUser.getUserRole().getRole());
			
			ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<UserResponseDTO>();
			responseStructure.setData(userResponse);
			responseStructure.setMessage("New user created");
			responseStructure.setStatusCode(HttpStatus.CREATED.value());
			//sendMail(userDTO.getEmail(), userDTO.getUserFirstName(), userDTO.getUserRole());
			return new ResponseEntity<ResponseStructure>(responseStructure, HttpStatus.CREATED);
	}
	
	@Override
	public ResponseEntity<ResponseStructure> sendMail(String to, String username, String role){
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Welcome");
		mailMessage.setText("Dear "+ username+" , your accout is successfully created as "+ role );
		javaMailSender.send(mailMessage);
		
		ResponseStructure responseStructure = new ResponseStructure<String>();
		responseStructure.setData(mailMessage);
		responseStructure.setMessage("mail sent successfully");
		responseStructure.setStatusCode(HttpStatus.OK.value());

		return new ResponseEntity<ResponseStructure>(responseStructure, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseStructure> userLogin(LoginRequestDTO loginRequestDTO) {
		User user = new User();
		User findUserByEmail = repo.findByEmail(loginRequestDTO.getEmail());
		
		if(findUserByEmail==null) {
			throw new EmailNotFoundException("Email Not Found in Database");
		}
		else if(!(findUserByEmail.getPassword().equals(loginRequestDTO.getPassword()))){
			throw new PasswordMismatchException("Password doesnot match");
		}
		
		
		UserResponseDTO userResponse = new UserResponseDTO();
		userResponse.setUserId(findUserByEmail.getUserId());
		userResponse.setUserFirstName(findUserByEmail.getUserFirstName());
		userResponse.setUserLastName(findUserByEmail.getUserLastName());
		userResponse.setEmail(findUserByEmail.getEmail());
		userResponse.setUserRole(findUserByEmail.getUserRole().getRole());
		
		ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<UserResponseDTO>();
		responseStructure.setData(userResponse);
		responseStructure.setMessage("Login Successfull");
		responseStructure.setStatusCode(HttpStatus.OK.value());		
		return new ResponseEntity<ResponseStructure>(responseStructure, HttpStatus.OK);
	}

	@Override
	public String generateResetToken() {
		String resetToken = UUID.randomUUID().toString();
		return resetToken;
	}

	@Override
	public String createPasswordResetLink(String resetToken) {
		return "http://localhost:3000/reset/";
	}
	
	@Override
	public ResponseEntity<ResponseStructure> sendPasswordLink(String to, String resetToken) {
		
		User user = repo.findByEmail(to);

		if(user==null) {
			throw new EmailNotFoundException("this email does not exist");
		}
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Link to reset password");
		mailMessage.setText("To reset your password, click the link below :\n"
							+ createPasswordResetLink(resetToken)+to);
		javaMailSender.send(mailMessage);
		
		ResponseStructure responseStructure = new ResponseStructure<String>();
		responseStructure.setData(mailMessage);
		responseStructure.setMessage("Password reset email sent.");
		responseStructure.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<ResponseStructure>(responseStructure, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseStructure> resetPassword(String email, String newPassword,String confirmPwd) {
		
		User user = repo.findByEmail(email);
		
			if(!newPassword.equals(confirmPwd)) {
				throw new PasswordMismatchException("both the password must be same");
			}
			if(user.getPassword().equalsIgnoreCase(newPassword)) {
				throw new CannotUseSamePasswordException("Try adding different password");		
			}
				
			user.setPassword(newPassword);
			repo.save(user);
		
		ResponseStructure responseStructure =new ResponseStructure();
		responseStructure.setData(user);
		responseStructure.setMessage("password reset successfully");
		responseStructure.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<ResponseStructure>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure> findUserById(Integer userId) {

		Optional<User> findUserById = repo.findById(userId);
		
		if(findUserById.isPresent()) {
			User user = findUserById.get();
			
			UserResponseDTO userResponseDTO= new UserResponseDTO();
			userResponseDTO.setUserId(userId);
			userResponseDTO.setUserFirstName(user.getUserFirstName());
			userResponseDTO.setUserLastName(user.getUserLastName());
			userResponseDTO.setEmail(user.getEmail());
			userResponseDTO.setUserRole(user.getUserRole().getRole());
			
			ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure();
			responseStructure.setData(userResponseDTO);
			responseStructure.setMessage("User Id Found");
			responseStructure.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure>(responseStructure, HttpStatus.FOUND);
		}
		
		else {
			throw new UserNotFoundByIdException("This User Id is not present");
		}
		
		
	}
	
	@Override
	public ResponseEntity<ResponseStructure> deleteUser(Integer userId) {
		Optional<User> findUserById = repo.findById(userId);
		 
		if(findUserById.isPresent()) {
			User user = findUserById.get();
			
			if(user.getUserRole().getRole().equals("Customer")) {
				user.setDeleteStatus(ISDELETED.TRUE);
				
				User saveUser = repo.save(user);
				ResponseStructure responseStructure = new ResponseStructure();
				responseStructure.setData(saveUser);
				responseStructure.setMessage("Marked as Deleted");
				responseStructure.setStatusCode(HttpStatus.OK.value());
				
				return new ResponseEntity<ResponseStructure>(responseStructure, HttpStatus.OK);
			}
			
			else {
				throw new ForbiddenOperationException("Admin cannot delete another Admin");
			}
				
		}
		
		else {
			throw new UserNotFoundByIdException("No such user Id found");
		}
	}
	

	@Override
	public List<User> findAllAdmins(String role) {
		UserRole userRole = roleRepo.findByRole(role);
		return repo.findByUserRole(userRole);
	}



	
}
