package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.User_Role;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.User_RoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserExistsException;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	public UserRepository repo;
	
	@Autowired
	public User_RoleRepository roleRepo;
	

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUserDetails(
			UserRequestDTO userRequest) {
		User save;
		User_Role userRole1;
			
		if(repo.findByUserEmail(userRequest.getUserEmail())==null){
			
			String role = userRequest.getUserRole();
			System.out.println(role);
			
			User_Role userRole = new User_Role();
			userRole.setUserRole(role);
			
			User user=new User();
			user.setUserFirstName(userRequest.getUserFirstName());
			user.setUserLastName(userRequest.getUserLastName());
			user.setRole(userRole);
			user.setUserEmail(userRequest.getUserEmail());
			user.setPassword(userRequest.getPassword());

			userRole1 = roleRepo.save(userRole);
			save = repo.save(user);
			
			System.out.println(user.toString());
			
			userRole1.getUserRole();
		}
		else {
			throw new UserExistsException("User already registered");
		}
		
		UserResponseDTO responseDTO = new UserResponseDTO();
		responseDTO.setUserId(save.getUserId());
		responseDTO.setUserFirstName(save.getUserFirstName());
		responseDTO.setUserLastName(save.getUserLastName());
		responseDTO.setUserRole(userRole1.getUserRole());
		responseDTO.setUserEmail(save.getUserEmail());
		responseDTO.setCreatedBy(save.getCreatedBy());
		responseDTO.setCreateDate(save.getCreateDate());
		responseDTO.setUpdatedBy(save.getUpdatedBy());
		responseDTO.setUpdatedDate(save.getUpdatedDate());
		
		ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<>();
		responseStructure.setData(responseDTO);
		responseStructure.setMessage("User registered successfully");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure, HttpStatus.CREATED);
	}

}
