package com.jspiders.ombs.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.exception.UserWithSameEmailExist;
import com.jspiders.ombs.repository.UserRepo;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	boolean flag;
	@Override
	public ResponseEntity<ResponseStructure> addUser(UserRequestDTO userRequestDTO) {
		User user = new User();
	
		user.setUserFirstName(userRequestDTO.getUserFirstName());
		user.setUserLastName(userRequestDTO.getUserLastName());
		user.setEmailId(userRequestDTO.getEmailId().toLowerCase());
		user.setPassword(userRequestDTO.getPassword());
		user.setUserRole(userRequestDTO.getUserRole());
           List<User> users = userRepo.findAll();
           for (User user2 : users) {
			if (user2.getEmailId().equals(user.getEmailId())) {
			   throw new UserWithSameEmailExist("Email id already in database");
			}
		}
		User user2 = userRepo.save(user);
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		userResponseDTO.setUserId(user2.getUserId());
		userResponseDTO.setUserFirstName(user2.getUserFirstName());
		userResponseDTO.setUserLastName(user2.getUserLastName());
		userResponseDTO.setEmailId(user2.getEmailId());
		
		
		ResponseStructure<UserResponseDTO>responseStructure = new ResponseStructure<UserResponseDTO>();
		responseStructure.setData(userResponseDTO);
		responseStructure.setMessage("user data added successfully");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		return  new ResponseEntity<ResponseStructure>(responseStructure,HttpStatus.CREATED);
	}
	
}
