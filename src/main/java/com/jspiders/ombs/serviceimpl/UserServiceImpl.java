package com.jspiders.ombs.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyExistException;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;

@Service
public class UserServiceImpl implements UserService {
	
	
	@Autowired
	private UserRepository repo;	
	
	@Autowired
	private UserRoleRepository repo1;
	
	@Override
	public  ResponseEntity<ResponseStructure<UserResponseDTO>> userSave(UserRequestDTO userRequest)  {
	
		List<User> userList=repo.findAll();
		User user = new User();
		
		if(userList!=null)
		{
			for(User ele:userList)
			{
				if(ele.getUserEmail().equalsIgnoreCase(userRequest.getUserEmail()))
				{
					throw new EmailAlreadyExistException("DATA IS NOT SAVED ");
				}
			}
		}
		
		List<UserRole> role = repo.getAllUserRoles();
		UserRole userRole = new UserRole();
		
		
		boolean flag=true;
		if(!role.isEmpty())
		{	
			
			for(UserRole roles:role)
			{
				if(roles.getUserRole().equalsIgnoreCase(userRequest.getUserRole()))
				{
					user.setUserRole1(roles);
					flag=false;
					break;
				}			
				
			}
			
			if(flag==true)
			{
				userRole.setUserRole(userRequest.getUserRole());
				repo1.save(userRole);
				user.setUserRole1(userRole);
			}
	
		}
		else
		{
			userRole.setUserRole(userRequest.getUserRole());
			repo1.save(userRole);
			user.setUserRole1(userRole);
		}
			
			
		
		
		user.setUserEmail(userRequest.getUserEmail().toLowerCase());
		user.setUserPassword(userRequest.getUserPassword());
		user.setUserFirstName(userRequest.getUserFirstName());
		user.setUserLastName(userRequest.getUserLastName());
		
		//user.setUserRole(userRequest.getUserRole());
		repo.save(user);

		UserResponseDTO response = new UserResponseDTO();
		response.setUserEmail(userRequest.getUserEmail());
		response.setUserFirstName(userRequest.getUserFirstName());
		response.setUserLastName(userRequest.getUserLastName());
		
		
		ResponseStructure<UserResponseDTO> responseStructure = new ResponseStructure<UserResponseDTO>();
		responseStructure.setData(response);
		responseStructure.setMessage("DATA SAVED SUCCESSFULL");
		responseStructure.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(responseStructure,HttpStatus.OK);
		
	}


}
