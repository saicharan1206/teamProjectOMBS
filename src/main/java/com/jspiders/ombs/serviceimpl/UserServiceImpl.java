package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserAlreadyExist;


@Service
public class UserServiceImpl implements UserService  {

	@Autowired
	private UserRepository repo;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request) {

		User save = null;
		try {
			User user = new User();
			user.setUserEmail(request.getUserEmail().toLowerCase());
			user.setUserPassword(request.getPassword());
			user.setUserFirstName(request.getUserFirstName());
			user.setUserLastName(request.getUserLastName());
			user.setRole(request.getRole());
			save = repo.save(user);
		} catch (Exception e) {
			throw new UserAlreadyExist("user already exits");
		}

		UserResponseDTO response = new UserResponseDTO();
		response.setUserId(save.getUserId());
		response.setCreatedBy(save.getCreatedBy());
		response.setCreatedDate(save.getCreatedDate());
		response.setUpdatedBy(null);
		response.setUpdatedDate(null);

		ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<>();
		responseStructure.setData(response);
		responseStructure.setMessage("created sucesfully");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity(responseStructure, HttpStatus.CREATED);

	}
}
