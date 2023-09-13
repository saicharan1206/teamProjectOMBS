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
import com.jspiders.ombs.util.exception.UserAlreadyExistException;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData(UserRequestDTO user) {
		User user2 = new User();
		if (repo.findByUserEmail(user.getUserEmail()) == null) {
			user2.setUserFirstName(user.getUserFirstName());
			user2.setUserLastName(user.getUserLastName());
			user2.setUserRole(user.getUserRole());
			user2.setUserEmail(user.getUserEmail().toLowerCase());
			user2.setUserPassword(user.getUserPassword());
			User user1 = repo.save(user2);

			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user1.getUserId());
			response.setUserFirstName(user1.getUserFirstName());
			response.setUserLastName(user1.getUserLastName());
			response.setUserRole(user1.getUserRole());
			response.setUserEmail(user1.getUserEmail());
			response.setUserPassword(user1.getUserPassword());
			response.setCreatedDate(user1.getCreatedDate());
			response.setCreatedBy(user1.getCreatedBy());
			response.setLastUpdatedBy(user1.getLastUpdatedBy());
			response.setLastUpdatedDate(user1.getLastUpdatedDate());

			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("user data sucessfully");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		} 
		else 
		{
			throw new UserAlreadyExistException("user is already exists");
		}

	}

}
