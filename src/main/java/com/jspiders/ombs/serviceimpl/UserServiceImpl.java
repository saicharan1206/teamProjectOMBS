package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserExistsException;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	public UserRepository repo;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUserDetails(
			com.jspiders.ombs.dto.UserRequestDTO userRequest) {
		User save;
		User user=new User();
		if(repo.findByUserEmail(userRequest.getUserEmail())==null){
			user.setUserFirstName(userRequest.getUserFirstName());
			user.setUserLastName(userRequest.getUserLastName());
			user.setUserRole(userRequest.getUserRole());
			user.setUserEmail(userRequest.getUserEmail().toLowerCase());
			user.setPassword(userRequest.getPassword());
			save = repo.save(user);
			System.out.println(user.toString());
		}
		else {
			throw new UserExistsException("User already registered");
		}
		
		UserResponseDTO responseDTO = new UserResponseDTO();
		responseDTO.setUserId(save.getUserId());
		responseDTO.setUserFirstName(save.getUserFirstName());
		responseDTO.setUserLastName(save.getUserLastName());
		responseDTO.setUserRole(save.getUserRole());
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
