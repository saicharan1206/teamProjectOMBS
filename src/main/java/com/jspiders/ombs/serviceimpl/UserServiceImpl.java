package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.IsDelete;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.User_Role;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailException;
import com.jspiders.ombs.util.exception.EmailNotFound;
import com.jspiders.ombs.util.exception.IdNotFoundException;
import com.jspiders.ombs.util.exception.PasswordDidNotMatchException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;
	@Autowired
	private UserRoleRepository roleRepo;
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest)
			throws MessagingException {

		User user = new User();
		if (repo.findByUserEmail(userRequest.getUserEmail()) == null) {
			User_Role userRole = roleRepo.getUserRoleByRole(userRequest.getUserRole());
			System.out.println(userRole);
			if (userRole == null) {
				userRole = new User_Role();
				userRole.setUserRole(userRequest.getUserRole());
				roleRepo.save(userRole);
			}
			user.setUserEmail(userRequest.getUserEmail().toLowerCase());
			user.setUserPassword(userRequest.getUserPassword());
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			user.setUserRole(userRole.getUserRole());
			user.setIsDelete(userRequest.getIsDelete().FALSE);
			user.setUserRloe(userRole);
			repo.save(user);

//			Send a mail

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper mimemessage = new MimeMessageHelper(message, false);
			mimemessage.setTo(userRequest.getUserEmail());
			String emailBody = "Hello " + userRequest.getFirstName() + " " + userRequest.getLastName()
					+ "is registered with OBMS App " + "<br><br><h4> Thanks and Regards <br>" + "Rudra <br>"
					+ "Bangalore <br>";
			mimemessage.setText(emailBody, true);
			mimemessage.setSentDate(new Date());
			javaMailSender.send(message);

		}

		else {
			throw new EmailException("Email is Already Present");
		}
		UserResponseDTO userResponse = new UserResponseDTO();
		userResponse.setUserId(user.getUserId());
		userResponse.setUserEmail(user.getUserEmail());
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setUserRloe(user.getUserRloe());
		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("User Saved Sucessfully");
		structure.setData(userResponse);

		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin(UserRequestDTO userRequest) {
		User user = null;
		user = repo.findByUserEmail(userRequest.getUserEmail());
		if (user != null) {
			if (user.getUserPassword().equals(userRequest.getUserPassword())) {
				UserResponseDTO userResponse = new UserResponseDTO();
				userResponse.setUserId(user.getUserId());
				userResponse.setFirstName(user.getFirstName());
				userResponse.setLastName(user.getLastName());
				userResponse.setUserRloe(user.getUserRloe());
				ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
				structure.setData(userResponse);
				structure.setMessage("LOGIN IS SUCESSFULL");
				structure.setStatusCode(HttpStatus.FOUND.value());
				return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.FOUND);
			} else {
				throw new PasswordDidNotMatchException("Password Did Not Match");
			}
		} else {
			throw new EmailNotFound("Email Not Found");
		}

	}

	@Override
	public ResponseEntity<String> changePassword(String email) throws MessagingException {
		User user = repo.findByUserEmail(email);
		if (user != null) {
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime, false);
			message.setTo(user.getUserEmail());
			message.setSubject("Password Link");
			String emailBody = "Hello" + user.getFirstName() + " " + user.getLastName()
					+ "You can cahnge the password using link" + "<a href=\"\">Forgot Password ?</a>" + "<h1>Thanks & Regards"
					+ "Rudra<h1>" + "img src=\"";
			message.setText(emailBody, true);
			message.setSentDate(new Date());
			javaMailSender.send(mime);
			return new ResponseEntity<String>("Email sent Sucessfully", HttpStatus.OK);
		} else {
			throw new EmailNotFound("Email is Invalid");
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<String>> isDeleted(int userId, String userPassword) {

		Optional<User> user = repo.findById(userId);
		if (user.isPresent()) {
			User user1 = user.get();
			if (user1.getUserPassword().equals(userPassword)) {
				user1.setIsDelete(user1.getIsDelete().TRUE);
				repo.save(user1);
				ResponseStructure<String> structure = new ResponseStructure<String>();
				structure.setData(userPassword);
				structure.setMessage("Data is deleted");
				structure.setStatusCode(HttpStatus.OK.value());
				return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.OK);

			}

			throw new PasswordDidNotMatchException("You Have Entred Wrong Password");

		}

		throw new IdNotFoundException("The User Id is Not Present");

	}

	@Override
	public ResponseEntity<ResponseStructure<String>> confirmPassword(String userEmail ,String newPassword)  {
		User user = repo.findByUserEmail(userEmail);
		if(user!=null)
		{  
			user.setUserPassword(newPassword);
		    repo.save(user);
		    ResponseStructure<String> structure = new ResponseStructure<String>();
		    structure.setStatusCode(HttpStatus.OK.value());
		    structure.setData(newPassword);
		    structure.setMessage("Password has been reset sucessfully");
		    return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
		} else {
			throw new EmailNotFound("The Email does not exist");
		}
		
		

		
	}



}
