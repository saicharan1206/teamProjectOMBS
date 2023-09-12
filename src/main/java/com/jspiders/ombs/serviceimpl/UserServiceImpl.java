package com.jspiders.ombs.serviceimpl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO) {
		String email=userRequestDTO.getUserEmail().toLowerCase();
		
		User user1 = userRepo.findByUserEmail(email);
		
		if (user1==null) {
			User user = new User();
			user.setUserEmail(email);
			user.setUserPassword(userRequestDTO.getUserPassword());
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			user.setUserRole(userRequestDTO.getUserRole());
			
			userRepo.save(user);
			
			UserResponseDTO respone = new UserResponseDTO();
			respone.setUserId(user.getUserId());
			respone.setUserEmail(user.getUserEmail());
			respone.setUserFirstName(user.getUserFirstName());
			respone.setUserLastName(user.getUserLastName());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("User data inserted Successfully!!!");
			structure.setData(respone);
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.CREATED);
		}
		else
			throw new EmailAlreadyExistException(email+" Email already exists!!!");
			
	}

	
}

