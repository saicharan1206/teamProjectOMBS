package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.LoginResponse;
import com.jspiders.ombs.dto.LoginVerification;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.User_Role;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.User_RoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserAlreadyExistsException;
import com.jspiders.ombs.util.exception.UserNotFoundByEmailException;
import com.jspiders.ombs.util.exception.WrongPasswordException;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private User_RoleRepository user_RoleRepository;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData (UserRequestDTO userRequestDTO) {
				
		User email = userRepository.findByUserEmail(userRequestDTO.getUserEmail());
		
		if (email == null)
		{
			User user = new User();
			
			user.setUserFirstName(userRequestDTO.getFirstName());
			user.setUserLastName(userRequestDTO.getLastName());
			user.setUserEmail(userRequestDTO.getUserEmail());
			user.setUserPassword(userRequestDTO.getUserPassword());
			
			
			if (user_RoleRepository.findByUserRole(userRequestDTO.getUser_Role().getUserRole()) != null)
			{
				User_Role user_Role = new User_Role();
				
				user_Role = user_RoleRepository.save(user_RoleRepository.findByUserRole(userRequestDTO.getUser_Role().getUserRole()));	
				
				user.setUser_Role(user_RoleRepository.findByUserRole(userRequestDTO.getUser_Role().getUserRole()));			

			}
			else
			{		
				User_Role user_Role = new User_Role();
				
				user_Role.setUserRole(userRequestDTO.getUser_Role().getUserRole());
				
				user_Role = user_RoleRepository.save(user_Role);
				
				user.setUser_Role(user_Role);			

			}
						
			
			User save = userRepository.save(user);
			
			UserResponseDTO responseDTO = new UserResponseDTO();
			
			responseDTO.setUserId(save.getUserId());
			responseDTO.setUserFirstName(save.getUserFirstName());
			responseDTO.setUserLastName(save.getUserLastName());
			responseDTO.setUserEmail(save.getUserEmail());
			responseDTO.setCreatedBy(save.getCreatedBy());
			responseDTO.setCreatedDate(save.getCreatedDate());
			responseDTO.setLastUpdatedBy(save.getLastUpdatedBy());
			responseDTO.setLastUpdatedDate(save.getLastUpdatedDate());
			responseDTO.setUserRole(save.getUser_Role());
			
			
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

	@Override
	public ResponseEntity<ResponseStructure<LoginResponse>> loginVer (LoginVerification loginVerification) {
		
		User findByUserEmail = userRepository.findByUserEmail(loginVerification.getUserEmail());
		
		if (findByUserEmail != null)
		{
			if (findByUserEmail.getUserPassword().equals(loginVerification.getUserPassword()))
			{
				String userRole = findByUserEmail.getUser_Role().getUserRole();
				
				LoginResponse loginResponse = new LoginResponse();
				loginResponse.setUserRole(userRole);
				
				ResponseStructure<LoginResponse> structure = new ResponseStructure<LoginResponse>();
				structure.setData(loginResponse);
				structure.setMessage("User Role is fetched successfully !!");
				structure.setStatusCode(HttpStatus.FOUND.value());
				
				return new ResponseEntity<ResponseStructure<LoginResponse>> (structure, HttpStatus.FOUND);
			}
			else
			{
				throw new WrongPasswordException ("Wrong Password !!");
			}
		}
		else
		{
			throw new UserNotFoundByEmailException("User not found by this mail id !!");
		}		
	}

}
