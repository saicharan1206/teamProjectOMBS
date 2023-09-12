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
import com.jspiders.ombs.util.exception.EmailException;

import java.util.List;
@Service
public class UserServiceImpl implements UserService{
@Autowired
	private UserRepository repo;
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest) {
	 List<User> users = repo.findAll();
		for (User user1 : users) {
			if (user1.getUserEmail().equalsIgnoreCase(userRequest.getUserEmail())) {
				throw new EmailException("emamil is already present");
			}
		}

		User user = new User();
		user.setUserEmail(userRequest.getUserEmail());
		user.setUserPassword(userRequest.getUserPassword());
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getFirstName());
		user.setUserRole(userRequest.getUserRole());
		User user2 = repo.save(user);

		UserResponseDTO userResponse = new UserResponseDTO();
		userResponse.setUserId(user2.getUserId());
		userResponse.setUserEmail(user2.getUserEmail());

		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
		structure.setData(userResponse);
		structure.setMessage("Data added");
		structure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);

	}
		
	
	

}
