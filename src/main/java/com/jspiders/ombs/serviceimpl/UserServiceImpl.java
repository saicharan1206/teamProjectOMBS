package com.jspiders.ombs.serviceimpl;

/*** 
 1. This is Service Implementation class which implements service interface
 2. private UserRepo variable is created and autowired
 3. Annotations
 	@Service - It is a stereotype annotations in Spring that help with 
 				component scanning and bean registration.
 			   It marks a class as a Spring-managed service or component
  	@Autowired - When you annotate a field, constructor, or method 
  				parameter with @Autowired, Spring will automatically 
  				search for a bean of the same type (or compatible type) 
  				to inject into that field, constructor, or parameter.
 4. There is a method by name createUser() which accepts UserRequestDTO as an input
 	parameter.
 5. Created the User Object and read email and password through UserRequestDTO
 	and set them in User entity class and convert the chars in email to lowercase
 6. repo.findAll() is used to find the data present in the database. it 
 	returns List of User entity
 7. Iterate the List using forEach compare the emails present in List and 
    the emails entered by user, if both are same throw the custom 
    emailIdAlreadyPresentException with a proper message.
 8. if both the emails are different then save the data into database 
 	using repo.save(user) it accepts user object as input. It 
 	
 ***/

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.UserRepo;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.emailIdAlreadyPresentException;



@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo repo;

	@Override
	public ResponseEntity<ResponseStructure> createUser(UserRequestDTO userDTO) {
		User user =new User();
		
		user.setUserFirstName(userDTO.getUserFirstName());
		user.setUserLastName(userDTO.getUserLastName());
		user.setUserRole(userDTO.getUserRole());
		user.setEmail(userDTO.getEmail().toLowerCase());
		user.setPassword(userDTO.getPassword());
	
		List<User> findAllUsers = repo.findAll();
		
		for (User user2 : findAllUsers) {
			if(user2.getEmail().equals(user.getEmail())) {
				throw new emailIdAlreadyPresentException("Email is already present in database");
			}
		}
		
			User saveUser = repo.save(user);
			UserResponseDTO userResponse = new UserResponseDTO();
			userResponse.setUserId(saveUser.getUserId());
			userResponse.setUserFirstName(saveUser.getUserFirstName());
			userResponse.setUserLastName(saveUser.getUserLastName());
			userResponse.setEmail(saveUser.getEmail());
			
			ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<UserResponseDTO>();
			responseStructure.setData(userResponse);
			responseStructure.setMessage("New user created");
			responseStructure.setStatusCode(HttpStatus.CREATED.value());
			
			return new ResponseEntity<ResponseStructure>(responseStructure, HttpStatus.CREATED);
	}

	 
}
