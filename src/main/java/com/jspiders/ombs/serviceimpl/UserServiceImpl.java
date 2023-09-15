package com.jspiders.ombs.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.IsDeleted;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyFoundException;
import com.jspiders.ombs.util.exception.UserNotFoundByEmail;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;

	@Autowired
	private UserRoleRepository reporole;
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO)
			throws MessagingException {

		String emailAddress = userRequestDTO.getEmailAddress().toLowerCase();
		String roleName = userRequestDTO.getRole().toLowerCase();
		User object = repo.findByEmailAddress(emailAddress);

		UserRole userRol = new UserRole();

		if (object == null) {
			userRol = reporole.findByRoleName(roleName);

			if (userRol == null) {
				userRol = new UserRole();
				userRol.setRoleName(roleName);
				reporole.save(userRol);
			}
			User user = new User();
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			user.setUserRole(userRol);
			user.setEmailAddress(userRequestDTO.getEmailAddress().toLowerCase());
			user.setPassword(userRequestDTO.getPassword());

			UserRole role = reporole.save(userRol);
			User user2 = repo.save(user);

			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user2.getUserId());
			response.setUserFirstName(user2.getUserFirstName());
			response.setUserRole(role.getRoleName());

			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("User Data Inserted Successfully");
			structure.setData(response);

			/***************** Mail : Account Created Successfully ************/

			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mime, true);

			messageHelper.setTo(userRequestDTO.getEmailAddress());
			messageHelper.setSubject("Acount Creation Response");

			String emailBody = "Hi," + user2.getUserFirstName() + ",Your Account has been created successfully."
					+ "<br><br><h4>Thanks & Regard</h4><br>" + "<h4>Jspider</h4><br>" + "<h4>http</h4>";
			messageHelper.setText(emailBody, true);
			messageHelper.setSentDate(new Date());

			javaMailSender.send(mime);

			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		}
		throw new EmailAlreadyFoundException("This Email is Not Applicable");
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> loginUser(String emailAddress, String password) {

		User user = repo.findByEmailAddress(emailAddress);
		ResponseStructure<String> structure = new ResponseStructure<String>();

		if (user != null) {
			System.out.println(emailAddress + " " + password);
			System.out.println(user.getEmailAddress() + " " + user.getPassword());

			String pass = user.getPassword();

			if (password.equals(pass)) {
				System.out.println("Password is Correct");

				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Successfully");
				structure.setData("Welcome Home," + user.getUserRole().getRoleName());

				return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.FOUND);
			}
			System.out.println("Password is Incorrect");
			structure.setStatusCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Sorry!! Re-enter");
			structure.setData("No User Found for this credentials");

			return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.NOT_FOUND);
		}
		throw new UserNotFoundByEmail("Invalid Email!!");
	}

	@Override
	public ResponseEntity<String> forgotPassword(String emailAddress) throws MessagingException {
		User user = repo.findByEmailAddress(emailAddress);

		if (user != null) {
			MimeMessage mime = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime, true);

			message.setTo(user.getEmailAddress());
			message.setSubject("Link To Create Password");

			String emailBody = "Hi" + user.getUserFirstName() + ",You can change the password using the below link."
					+ "<br><br><h4>Thanks & Regard</h4><br>" + "<h4>Jspider</h4><br>" + "<h4>http</h4>";
			message.setText(emailBody, true);
			message.setSentDate(new Date());

			javaMailSender.send(mime);
			return new ResponseEntity<String>("Mail Has been Sent,check Your mail", HttpStatus.OK);
		}
		throw new UserNotFoundByEmail("Please, Enter Valid Email");
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO userRequestDTO, int userId) {
		Optional<User> optional = repo.findById(userId);
		if (optional.isPresent()) {
			User exUser = optional.get();
			User user = new User();
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			user.setUserRole(exUser.getUserRole());
			user.setEmailAddress(userRequestDTO.getEmailAddress());
			user.setPassword(userRequestDTO.getPassword());

			user.setUserId(userId);
			User user2 = repo.save(user);

			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();

			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user2.getUserId());
			response.setUserFirstName(user2.getUserFirstName());
			response.setUserRole(user2.getUserRole().getRoleName());

			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			structure.setMessage("User Data Updated Successfully!!!!");
			structure.setData(response);

			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);
		} else {
			return null;
		}
	}
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteUser(int userId) {
		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
		Optional<User> optional = Optional.of(repo.findById(userId).orElse(null));
		if (optional.isPresent()) {
			User user = optional.get();
			user.setDeleted(IsDeleted.TRUE);
			user = repo.save(user);
			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user.getUserId());
			response.setUserFirstName(user.getUserFirstName());
			response.setUserRole(user.getUserRole().getRoleName());

			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("User Data Successfully Deleted!!");
			structure.setData(response);
		
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(HttpStatus.OK);
		} else {
			throw new UserNotFoundByIdException("Failed To Delete the User Details");
		}
	}
	
	@Override
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUser() {
		List<User> users = repo.findAll();
		List<UserResponseDTO> responses = new ArrayList<>();
		for (User user : users) {
			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user.getUserId());
			response.setUserFirstName(user.getUserFirstName());
			response.setUserRole(user.getUserRole().getRoleName());
			
			responses.add(response);
		}
		ResponseStructure<List<UserResponseDTO>> structure = new ResponseStructure<List<UserResponseDTO>>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("User Details!!!!");
		structure.setData(responses);
		return new ResponseEntity<ResponseStructure<List<UserResponseDTO>>>(structure, HttpStatus.OK);
	}

}
