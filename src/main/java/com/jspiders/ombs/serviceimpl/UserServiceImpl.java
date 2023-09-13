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
import com.jspiders.ombs.util.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData (UserRequestDTO userRequestDTO) {
				
		User email = userRepository.findByUserEmail(userRequestDTO.getUserEmail());
		
		if (email == null)
		{
			User user = new User();
			
			user.setUserFirstName(userRequestDTO.getFirstName());
			user.setUserLastName(userRequestDTO.getLastName());
			user.setUserRole(userRequestDTO.getUserRole());
			user.setUserEmail(userRequestDTO.getUserEmail());
			user.setUserPassword(userRequestDTO.getUserPassword());
			
			
			User save = userRepository.save(user);
			
			UserResponseDTO responseDTO = new UserResponseDTO();
			
			responseDTO.setUserId(save.getUserId());
			responseDTO.setUserFirstName(save.getUserFirstName());
			responseDTO.setUserLastName(save.getUserLastName());
			responseDTO.setUserEmail(save.getUserEmail());
			responseDTO.setUserRole(save.getUserRole());
			responseDTO.setCreatedBy(save.getCreatedBy());
			responseDTO.setCreatedDate(save.getCreatedDate());
			responseDTO.setLastUpdatedBy(save.getLastUpdatedBy());
			responseDTO.setLastUpdatedDate(save.getLastUpdatedDate());
			
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			
			structure.setData(responseDTO);
			structure.setMessage("Data saved successfully !!");
			structure.setStatusCode(HttpStatus.CREATED.value());
			
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		}
		else
		{
			throw new UserAlreadyExistsException("User is already exists !!");
		}
	}

}
