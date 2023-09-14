package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
			//role2.getUser().add(user);
			repository.save(role);
			save = repo.save(user);

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
}
