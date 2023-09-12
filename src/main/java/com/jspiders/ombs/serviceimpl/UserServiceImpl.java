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
import com.jspiders.ombs.util.exception.UserAlreadyExist;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository repository;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request) {
		User save = null;
		try {
			User user = new User();
			user.setUserEmail(request.getUserEmail().toLowerCase());
			user.setUserPassword(request.getUserPassword());
			user.setUserFirstName(request.getUserFirstName());
			user.setUserLastName(request.getUserLastName());
			user.setRole(request.getRole());
			save=repository.save(user);
		}
		catch (Exception e) {
			throw new UserAlreadyExist("User Data Already Exist in the Database");
		}
		UserResponseDTO dto=new UserResponseDTO();
		dto.setUserId(save.getUserId());
		dto.setCreatedDate(save.getCreatedDate());
		dto.setCreatedBy(save.getCreatedBy());
		dto.setUpdatedDate(null);
		dto.setUpdatedBy(null);
		
		ResponseStructure<UserResponseDTO> structure=new ResponseStructure<>();
		structure.setData(dto);
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Response Generated Successfully");
		return new ResponseEntity(structure,HttpStatus.CREATED);
	}

}
