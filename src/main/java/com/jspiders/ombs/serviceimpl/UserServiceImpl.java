package com.jspiders.ombs.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.LoginRequest;
import com.jspiders.ombs.dto.LoginResponse;
import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepo;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserAlreadyExistException;
import com.jspiders.ombs.util.exception.UserDoesNotExistException;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private UserRoleRepo userRoleRepo;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveData(UserRequestDTO user) {
		User user2 = new User();
		if (repo.findByUserEmail(user.getUserEmail()) == null) {
			user2.setUserFirstName(user.getUserFirstName());
			user2.setUserLastName(user.getUserLastName());
			user2.setUserEmail(user.getUserEmail().toLowerCase());
			user2.setUserPassword(user.getUserPassword());
			
			String userRoleName = user.getUserRole().getUserRoleName();
			UserRole userRole = userRoleRepo.findByUserRoleName(userRoleName);
			
			if(userRole==null)
			{
				UserRole ur = new UserRole();
				ur.setUserRoleName(userRoleName);
				userRole = userRoleRepo.save(ur);
				user2.setUserRole(userRole);
			}
			else {
				user2.setUserRole(userRole);
			}
			
			User user1 = repo.save(user2);

			UserResponseDTO response = new UserResponseDTO();
			response.setUserId(user1.getUserId());
			response.setUserFirstName(user1.getUserFirstName());
			response.setUserLastName(user1.getUserLastName());
			response.setUserEmail(user1.getUserEmail());
			response.setUserPassword(user1.getUserPassword());
			response.setUserRole(user1.getUserRole());
			response.setCreatedDate(user1.getCreatedDate());
			response.setCreatedBy(user1.getCreatedBy());
			response.setLastUpdatedBy(user1.getLastUpdatedBy());
			response.setLastUpdatedDate(user1.getLastUpdatedDate());

			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("user data sucessfully");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		} 
		else 
		{
			throw new UserAlreadyExistException("user is already exists");
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<LoginResponse>> loginUser(LoginRequest login) 
	{
		User user = repo.findByUserEmail(login.getUserEmail());
		if(user!=null)
		{
			if (user.getUserPassword().equals(login.getUserPassword()))
			{
				String userRoleName = user.getUserRole().getUserRoleName();
			    if(userRoleName.equals("Admin"))
				{
			    	LoginResponse response = new LoginResponse();
			    	response.setUserRoleName(userRoleName);
			    	ResponseStructure<LoginResponse> structure = new ResponseStructure<LoginResponse>();
					structure.setStatusCode(HttpStatus.CREATED.value());
					structure.setMessage("Admin login sucessfully");
					structure.setData(response);
					return new ResponseEntity<ResponseStructure<LoginResponse>>(structure, HttpStatus.OK);
				}
				else
				{
			    	LoginResponse response = new LoginResponse();
			    	response.setUserRoleName(userRoleName);
			    	ResponseStructure<LoginResponse> structure = new ResponseStructure<LoginResponse>();
					structure.setStatusCode(HttpStatus.CREATED.value());
					structure.setMessage("User login sucessfully");
					structure.setData(response);
					return new ResponseEntity<ResponseStructure<LoginResponse>>(structure, HttpStatus.OK);
				}
			}
			else
			{
//				throw new WrongPasswordException ("Wrong Password !!");
				return null;
			}		
		}
		else
		{
			throw new UserDoesNotExistException("user does not exists");
		}
		
	}
	
	

}
