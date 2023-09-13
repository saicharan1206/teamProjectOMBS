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
import com.jspiders.ombs.util.exception.UserExistsException;


@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRepo;
	@Autowired
	UserRoleRepository userRoleRepository; 
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO) {
		User save;
		User user=new User();
		
		if(userRepo.findByUserEmail(userRequestDTO.getUserEmail())==null){
			user.setUserEmail(userRequestDTO.getUserEmail().toLowerCase());
			user.setUserPassword(userRequestDTO.getUserPassword());
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			
			String userRoleName = userRequestDTO.getUserRole().getUserRoleName();
			System.out.println(userRoleName);
			UserRole userRole = userRoleRepository.findByUserRoleName(userRoleName);
			
			if(userRole==null)
			{
				UserRole ur = new UserRole();
				ur.setUserRoleName(userRoleName);
				ur.setCreatedBy(ur.getCreatedBy());
				ur.setCreatedDate(ur.getCreatedDate());
				ur.setUpdatedBy(ur.getUpdatedBy());
				ur.setUpdatedDate(ur.getUpdatedDate());
				userRole = userRoleRepository.save(ur);
				user.setUserRole(userRole);
			}
			else
			{
				user.setUserRole(userRole);
			}
		//	user.setUserRole(userRequestDTO.getUserRole().getUserRoleName());
		//	user.setUserRole(userRequestDTO.getUserRole());
			save = userRepo.save(user);
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
		responseDTO.setCreatedDate(save.getCreatedDate());
		responseDTO.setUpdatedBy(save.getUpdatedBy());
		responseDTO.setUpdatedDate(save.getUpdatedDate());
		
		ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<>();
		responseStructure.setData(responseDTO);
		responseStructure.setMessage("User registered successfully");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure, HttpStatus.CREATED);
	}
	

}
