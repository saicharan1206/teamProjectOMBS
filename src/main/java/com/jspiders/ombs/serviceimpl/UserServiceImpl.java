package com.jspiders.ombs.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepositary;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserRoleRepositary roleRepo;
	
	

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequestDTO userRequest) {

		User user1;
		User user = new User();
		UserRole  userRole = new UserRole();
		
		if (userRepo.findByUserEmail(userRequest.getUserEmail()) == null) {
			userRole = roleRepo.getUserRoleByRole(userRequest.getUserRole());
			System.out.println(userRole);
			if(userRole == null) {
			  userRole = new UserRole();
			  userRole.setUserRole(userRequest.getUserRole());
				roleRepo.save(userRole);
			}

			user.setUserEmail(userRequest.getUserEmail().toLowerCase());
			user.setUserPassord(userRequest.getUserPassord());
			user.setUserFirstName(userRequest.getUserFirstName());
			user.setUserLastName(userRequest.getUserLastName());
			user.setRole(userRole);
			 userRepo.save(user);
		} else {
			throw new EmailException("email is already Present");
		}

		UserResponse response = new UserResponse();
		response.setUserId(user.getUserId());
		response.setUserEmail(user.getUserEmail());
		ResponseStructure<UserResponse> structure = new ResponseStructure<UserResponse>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("User Saved Succssefully");
		structure.setData(response);
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.OK);
	}

}

	

