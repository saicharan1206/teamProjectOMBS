package com.jspiders.ombs.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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

import com.jspiders.ombs.util.exception.UserNotFoundByIdException;
import com.jspiders.ombs.util.exception.UserNotFoundException;

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
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(String email, String password) {
		User user = repo.findByUserEmail(email);

		if (user != null && user.getPassword().equals(password)) {
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
	public ResponseEntity<ResponseStructure<ForgotEmailResponse>> getUserByEmail(String email) {
		User user = repo.findByUserEmail(email);

		if (user != null) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(user.getUserEmail());
			message.setSubject("update password ");
			message.setText("Please click below link to set new password " + "\n\nThanks & Regards" + "\n"
					+ user.getCreatedBy() + "\n");
			message.setSentDate(new Date());
			javaMailSender.send(message);

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
	if (user != null && user.getRole().getUser().equals("admin")) {
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
		structure.setMessage("User data updated successfully!!!");
		structure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> findAllUser() {
		List<User> findAll = repo.findAll();
		if(!findAll.isEmpty())
		{
			Vector<UserResponseDTO> usersDtos = new Vector();
			for (User user : findAll) {
				if(user.getRole().getUserRole().equalsIgnoreCase("admin"))
				{
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
			structure.setMessage("User data deleted successfully!!!");
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<List<UserResponseDTO>>>(structure, HttpStatus.CREATED);
		}
		throw new UserNotFoundException("User not found!!");
	}

	


}
