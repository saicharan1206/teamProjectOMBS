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
import com.jspiders.ombs.util.exception.UserExistsException;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepo;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO user) {
		User user2 = new User();
		if(userRepo.findByUserEmail(user.getUserEmail())==null)
		{
			user2.setUserEmail(user.getUserEmail().toLowerCase());
			user2.setUserPassword(user.getUserPassword());
			user2.setUserFristName(user.getUserFristName());
			user2.setUserLastName(user.getUserLastName());
			user2.setUserRole(user.getUserRole());
			User user1 = userRepo.save(user2);
			
			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user1.getUserId());
			response.setUserEmail(user1.getUserEmail());
			response.setUserPassword(user1.getUserPassword());
			response.setCreatedDate(user1.getCreatedDate());
			response.setCreatedBy(user1.getCreatedBy());
			response.setUpdatedDate(user1.getUpdatedDate());
			response.setUpdatedBy(user1.getUpdatedBy());
			response.setUserFristName(user1.getUserFristName());
			response.setUserLastName(user1.getUserLastName());
			response.setUserRole(user1.getUserRole());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("user data sucessfully");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.CREATED);
		}
		else
		{
			throw new UserExistsException("user is already exists");
		}
	}

	
}
