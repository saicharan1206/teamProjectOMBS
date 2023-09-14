package com.jspiders.ombs.serviceimpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.ForgotEmailResponse;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserAlreadyExist;
import com.jspiders.ombs.util.exception.UserNotFoundException;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repo;
	@Autowired
	private UserRoleRepository repository;
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request) {
		UserRole role = repository.findByRole(request.getRole());

		UserRole role2 = null;
		if (role == null) {
			role2 = new UserRole();
			role2.setRole(request.getRole());
			role = role2;
		}
		User save = null;
		try {
			User user = new User();
			user.setUserEmail(request.getUserEmail().toLowerCase());
			user.setUserPassword(request.getUserPassword());
			user.setUserFirstName(request.getUserFirstName());
			user.setUserLastName(request.getUserLastName());
			user.setRole(role);
			repository.save(role);
			save = repo.save(user);
			
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(save.getUserEmail());
			message.setSubject("Account created sucessfully");
			message.setText("account created sucessfully with "+save.getRole().getRole()+"\n\n Thanks & Regards"+save.getCreatedBy());
			message.setSentDate(new Date());
			
			javaMailSender.send(message);
			

		} catch (Exception e) {
			throw new UserAlreadyExist("user already exits");
		}
		
		

		UserResponseDTO response = new UserResponseDTO();
		response.setUserId(save.getUserId());
		response.setCreatedBy(save.getCreatedBy());
		response.setCreatedDate(save.getCreatedDate());
		response.setUserEmail(save.getUserPassword());
		response.setRole(save.getRole().getRole());
		response.setUpdatedBy(save.getLastmodifiedby());
		response.setUpdatedDate(save.getLastmodifieddate());
		response.setUserEmail(save.getUserEmail());

		ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<>();
		responseStructure.setData(response);
		responseStructure.setMessage("created sucesfully");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity(responseStructure, HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(String email, String password) {
		User user = repo.findByUserEmail(email);
		if (user != null && user.getUserPassword().equals(password)) {
			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user.getUserId());
			response.setCreatedBy(user.getCreatedBy());
			response.setCreatedDate(user.getCreatedDate());
			response.setUserEmail(user.getUserPassword());
			response.setRole(user.getRole().getRole());
			response.setUpdatedBy(user.getLastmodifiedby());
			response.setUpdatedDate(user.getLastmodifieddate());
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
	public ResponseEntity<ResponseStructure<ForgotEmailResponse>> sendforgotemail(String email) {
		User userEmail = repo.findByUserEmail(email);
		if(userEmail !=null) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(userEmail.getUserEmail());
			message.setSubject("password change");
			message.setText("click below email to change the password "+"\n\n Thanks & Regards"+"\n"+userEmail.getCreatedBy());
			message.setSentDate(new Date());
			
			javaMailSender.send(message);
			
			ForgotEmailResponse emailResponse = new ForgotEmailResponse();
			emailResponse.setUserName(userEmail.getUserEmail());
			emailResponse.setCreatedby(userEmail.getCreatedBy());
			
			ResponseStructure<ForgotEmailResponse> responseStructure = new ResponseStructure<>();
			responseStructure.setData(emailResponse);
			responseStructure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity(responseStructure, HttpStatus.OK);
		}
		
		throw new UserNotFoundException("no user with name"+email);
	}

	
}
