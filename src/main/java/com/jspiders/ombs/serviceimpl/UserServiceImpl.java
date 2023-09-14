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
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;
import com.jspiders.ombs.util.exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private UserRoleRepository repo1;
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request) {
		User email = repo.findByUserEmail(request.getUserEmail().toLowerCase());
		UserRole userRole = repo1.findByUserRole(request.getUserRole());
		
		if(userRole==null)
		{
			UserRole role=new UserRole();
			role.setUserRole(request.getUserRole());
			repo1.save(role);
			userRole=role;
		}
		
		if(email!=null)
		{
			throw new UserNotFoundByIdException("mailId is already exist!!");
		}
			User user = new User();
			user.setUserEmail(request.getUserEmail().toLowerCase());
			user.setPassword(request.getPassword());
			user.setUserFirstName(request.getUserFirstName());
			user.setUserLastName(request.getUserLastName());
			user.setRole(userRole);
			
				 user= repo.save(user);
			
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
			
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.CREATED);	
		
	}
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> getUser(String email, String password) {
		User user = repo.findByUserEmail(email);
		
		if (user != null && user.getPassword().equals(password)) {
			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user.getUserId());
			response.setUserFirstName(user.getUserFirstName());
			response.setUserLastName(user.getUserLastName());
			response.setCreatedBy(user.getCreatedBy());
			response.setCreatedDateTime(user.getCreatedDateTime());
			response.setUserEmail(user.getUserEmail());
			
			response.setUpdatedDateTime(user.getUpdatedDateTime());
			response.setUpdatedBy(user.getUpdatedBy());
			response.setUserRole(user.getRole().getUserRole());
		

			ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<>();
			responseStructure.setData(response);
			responseStructure.setMessage("found sucesfull");
			responseStructure.setStatusCode(HttpStatus.FOUND.value());

			return new ResponseEntity(responseStructure, HttpStatus.FOUND);
		}

		throw new UserNotFoundException("user not found");
	}
}