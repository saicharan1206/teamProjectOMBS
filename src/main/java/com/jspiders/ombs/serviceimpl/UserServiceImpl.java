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
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository repo;
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request) {
			User user = new User();
			user.setUserEmail(request.getUserEmail().toLowerCase());
			user.setPassword(request.getPassword());
			user.setUserFirstName(request.getUserFirstName());
			user.setUserLastName(request.getUserLastName());
			user.setUserRole(request.getUserRole());
			 try {
				 user= repo.save(user);
			 }
			 catch (Exception e) {
				throw new UserNotFoundByIdException("mailId is already exist!!");
			}
			UserResponseDTO responseDTO = new UserResponseDTO();
			responseDTO.setUserId(user.getUserId());
			responseDTO.setUserEmail(user.getUserEmail());
			responseDTO.setCreatedDateTime(user.getCreatedDateTime());
			responseDTO.setCreatedBy(user.getCreatedBy());
			responseDTO.setUserFirstName(user.getUserFirstName());
			responseDTO.setUserLastName(user.getUserLastName());
			responseDTO.setUserRole(user.getUserRole());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
			structure.setData(responseDTO);
			structure.setMessage("user data added successfully!!!");
			structure.setStatusCode(HttpStatus.CREATED.value());
			
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.CREATED);	
		
	}
}
