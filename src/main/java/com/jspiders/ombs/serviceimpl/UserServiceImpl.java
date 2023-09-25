package com.jspiders.ombs.serviceimpl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.ForgotEmailResponse;
import com.jspiders.ombs.dto.LoginDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.enums.IsDeleted;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.InvalidUserException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;
import com.jspiders.ombs.util.exception.UserNotFoundException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;
	@Autowired
	private UserRoleRepository repo1;
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request) {
		User email = repo.findByUserEmail(request.getUserEmail().toLowerCase());
		UserRole userRole = repo1.findByUserRole(request.getUserRole());
		if (userRole == null) {
			UserRole role = new UserRole();
			role.setUserRole(request.getUserRole());
			repo1.save(role);
			userRole = role;
		}
		if (email != null) {
			throw new UserNotFoundByIdException("mailId is already exist!!");
		}
		User user = new User();
		user.setUserEmail(request.getUserEmail().toLowerCase());
		user.setPassword(request.getPassword());
		user.setUserFirstName(request.getUserFirstName());
		user.setUserLastName(request.getUserLastName());
		user.setRole(userRole);
		user.setIsDeleted(IsDeleted.FALSE);
		user = repo.save(user);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getUserEmail());
		message.setSubject("Account created successfully ");
		message.setText("Account has been created as " + user.getRole().getUserRole() + "\n\nThanks & Regards" + "\n"
				+ user.getCreatedBy() + "\n");
		message.setSentDate(new Date());
		javaMailSender.send(message);

		UserResponseDTO responseDTO = new UserResponseDTO();
		responseDTO.setUserId(user.getUserId());
		responseDTO.setUserEmail(user.getUserEmail());
		responseDTO.setCreatedDateTime(user.getCreatedDateTime());
		responseDTO.setCreatedBy(user.getCreatedBy());
		responseDTO.setUserFirstName(user.getUserFirstName());
		responseDTO.setUserLastName(user.getUserLastName());

		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
		structure.setData(responseDTO);
		structure.setMessage("user data added successfully!!!");
		structure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUserByEmailByPassword(LoginDTO login) {
		User user = repo.findByUserEmail(login.getUserEmail());

		if (user != null && user.getPassword().equals(login.getPassword()) ) {
			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user.getUserId());
			response.setCreatedBy(user.getCreatedBy());
			response.setCreatedDateTime(user.getCreatedDateTime());
			response.setUserEmail(user.getUserEmail());
			response.setUserRole(user.getRole().getUserRole());
			response.setUpdatedDateTime(user.getUpdatedDateTime());
			response.setUpdatedBy(user.getUpdatedBy());
			response.setUserEmail(user.getUserEmail());

			ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<>();
			responseStructure.setData(response);
			responseStructure.setMessage("found sucesfull");
			responseStructure.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity(responseStructure, HttpStatus.FOUND);
		}
		throw new UserNotFoundException("user not found");
	}

	@Override
	public ResponseEntity<ResponseStructure<ForgotEmailResponse>> sendConfirmationEntityMail(String email) throws MessagingException {
		User user = repo.findByUserEmail(email);

		if (user != null) {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
			helper.setSentDate(new Date());
			helper.setTo(user.getUserEmail());
			helper.setSubject("update password ");
			 String emailBody = "Please click below link to set new password "+"<br>"+" <a href=\"http://localhost:3000/pwd/"+user.getUserEmail()+"\">click here</a>"+"<h4>Thanks & Regards<br>"
					 			+user.getCreatedBy()+"<br>"+"</h4>"
					 			+"<img src=\"https://www.jspiders.com/_nuxt/img/logo_jspiders.3b552d0.png\" width=\"250\" />";
			helper.setText(emailBody,true);
			helper.setSentDate(new Date());
			
			javaMailSender.send(mimeMessage);
			
			ForgotEmailResponse emailResponse = new ForgotEmailResponse();
			emailResponse.setUserId(user.getUserId());
			emailResponse.setCreatedBy(user.getCreatedBy());
			emailResponse.setUserEmail(user.getUserEmail());
			emailResponse.setCreatedDateTime(user.getCreatedDateTime());

			ResponseStructure<ForgotEmailResponse> responseStructure = new ResponseStructure<>();
			responseStructure.setData(emailResponse);
			responseStructure.setMessage("found sucesfull");
			responseStructure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity(responseStructure, HttpStatus.OK);
		}
		throw new UserNotFoundException("user not found");
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteByEmail(String email) {
		User user = repo.findByUserEmail(email);
		if (user != null && user.getRole().getUserRole().equalsIgnoreCase("admin")) {
			user.setIsDeleted(IsDeleted.TRUE);
			repo.save(user);

			UserResponseDTO responseDTO = new UserResponseDTO();
			responseDTO.setUserId(user.getUserId());
			responseDTO.setUserEmail(user.getUserEmail());
			responseDTO.setCreatedDateTime(user.getCreatedDateTime());
			responseDTO.setCreatedBy(user.getCreatedBy());
			responseDTO.setUserFirstName(user.getUserFirstName());
			responseDTO.setUserLastName(user.getUserLastName());

			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
			structure.setData(responseDTO);
			structure.setMessage("user data deleted successfully!!!");
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		}
		throw new InvalidUserException("Email not found  or User as no authoriztion to delete the data");

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateByEmail(@Valid UserRequestDTO request) {
		User user = repo.findByUserEmail(request.getUserEmail().toLowerCase());
		UserRole userRole = repo1.findByUserRole(request.getUserRole());
		if (user == null) {
			throw new UserNotFoundByIdException("EmailId is already exist!!");
		}
		user.setUserEmail(request.getUserEmail().toLowerCase());
		user.setPassword(request.getPassword());
		user.setUserFirstName(request.getUserFirstName());
		user.setUserLastName(request.getUserLastName());
		user.setRole(userRole);
		user.setIsDeleted(IsDeleted.FALSE);
		user = repo.save(user);

		UserResponseDTO responseDTO = new UserResponseDTO();
		responseDTO.setUserId(user.getUserId());
		responseDTO.setUserEmail(user.getUserEmail());
		responseDTO.setCreatedDateTime(user.getCreatedDateTime());
		responseDTO.setCreatedBy(user.getCreatedBy());
		responseDTO.setUserFirstName(user.getUserFirstName());
		responseDTO.setUserLastName(user.getUserLastName());
		responseDTO.setUserRole(user.getRole().getUserRole());

		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
		structure.setData(responseDTO);
		structure.setMessage("User data updated successfully!!!");
		structure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> getAllUsers() {
		List<User> findAll = repo.findAll();
		if (!findAll.isEmpty()) {
			Vector<UserResponseDTO> usersDtos = new Vector();
			for (User user : findAll) {
				if (user.getRole().getUserRole().equalsIgnoreCase("admin") && user.getIsDeleted().equals(IsDeleted.FALSE)) {
					UserResponseDTO response = new UserResponseDTO();
					response.setUserId(user.getUserId());
					response.setCreatedBy(user.getCreatedBy());
					response.setCreatedDateTime(user.getCreatedDateTime());
					response.setUserEmail(user.getUserEmail());
					response.setUserRole(user.getRole().getUserRole());
					response.setUpdatedDateTime(user.getUpdatedDateTime());
					response.setUpdatedBy(user.getUpdatedBy());
					response.setUserEmail(user.getUserEmail());
					response.setUserFirstName(user.getUserFirstName());
					response.setUserLastName(user.getUserLastName());

					usersDtos.add(response);
				}
			}
			ResponseStructure<List<UserResponseDTO>> structure = new ResponseStructure<>();
			structure.setData(usersDtos);
			structure.setMessage("User data fetched successfully!!!");
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<List<UserResponseDTO>>>(structure, HttpStatus.CREATED);
		}
		throw new UserNotFoundException("User not found!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> resetPassword(String email, String pwd) {
		User user = repo.findByUserEmail(email.toLowerCase());
		if(user!=null )
		{
			user.setPassword(pwd);
			repo.save(user);
			
			UserResponseDTO responseDTO = new UserResponseDTO();
			responseDTO.setUserId(user.getUserId());
			responseDTO.setUserEmail(user.getUserEmail());
			responseDTO.setCreatedDateTime(user.getCreatedDateTime());
			responseDTO.setCreatedBy(user.getCreatedBy());
			responseDTO.setUserFirstName(user.getUserFirstName());
			responseDTO.setUserLastName(user.getUserLastName());
			responseDTO.setUserRole(user.getRole().getUserRole());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
			structure.setData(responseDTO);
			structure.setMessage("User password updated successfully!!!");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.ACCEPTED);
		}
		throw new UserNotFoundException("User not found!!");
	}

}
