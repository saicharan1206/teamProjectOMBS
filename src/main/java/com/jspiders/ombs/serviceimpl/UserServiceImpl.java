package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponse;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.UserRepo;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserAlreadyExistsException;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepo userRepo;
	@Autowired
	UserRoleRepository userRoleRepository;
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequestDTO userRequestDTO) {
		User save;
		User user=new User();
		
		UserRole userRole1;
		
		if(userRepo.findByUserEmail(userRequestDTO.getUserEmail())==null){
			
			String role = userRequestDTO.getUserRole();
			System.out.println(role);
			
			UserRole userRole = new UserRole();
			userRole.setRole(role);
			
			user.setUserEmail(userRequestDTO.getUserEmail().toLowerCase());
			user.setUserpassword(userRequestDTO.getUserPassword());
			user.setUserrole(userRole);
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			
			userRole1 = userRoleRepository.save(userRole);

			save = userRepo.save(user);
			System.out.println(user.toString());
			userRole1.getRole();
		}
		else {
			throw new UserAlreadyExistsException("User already exists!!");
		}
		
		UserResponse responseDTO = new UserResponse();
		responseDTO.setUserId(save.getUserId());
		responseDTO.setUserFirstName(save.getUserFirstName());
		responseDTO.setUserLastName(save.getUserLastName());
		responseDTO.setUserRole(userRole1.getRole());
		responseDTO.setUserEmail(save.getUserEmail());
		responseDTO.setCreatedBy(save.getCreatedBy());
		responseDTO.setCreatedDate(save.getCreatedDate());
		responseDTO.setUpdatedBy(save.getUpdatedBy());
		responseDTO.setUpdatedDate(save.getUpdatedDate());
		
		ResponseStructure<UserResponse> responseStructure = new ResponseStructure<>();
		responseStructure.setData(responseDTO);
		responseStructure.setMessage("User registered successfully!!!");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		
		return new ResponseEntity<ResponseStructure<UserResponse>>(responseStructure, HttpStatus.CREATED);
	}
	
	
}
