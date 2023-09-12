package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.UserAlreadyExists;
@Service
public class UserServiceImpl implements UserService
{

	@Autowired
	private UserRepository repo;
	@Autowired
	private UserResponseDTO responseDTO;

	
	@Override
	public ResponseStructure<UserResponseDTO> saveUser(UserRequestDTO userdata) 
	{
		   String email = userdata.getUserEmail().toLowerCase();
		   User userExist = repo.findByUserEmail(email);
		   if(userExist!=null)
		   {
			   throw new UserAlreadyExists(email+"  User Already Exist ");
		   }
		    User user1=new User();
		    user1.setUserFirstName(userdata.getUserFirstName());
		    user1.setUserLastName(userdata.getUserLastName());
		    user1.setRole(userdata.getRole());
			user1.setUserEmail(email);
			user1.setUserPassword(userdata.getUserPassword());
			
			
		    User user = repo.save(user1);
		
		   
			responseDTO.setUserId(user.getUserId());
			responseDTO.setUserFirstName(user.getUserFirstName());
			responseDTO.setUserLastName(user.getUserLastName());
			responseDTO.setRole(user.getRole());
			responseDTO.setUserEmail(user.getUserEmail());
			responseDTO.setCreatedBy(user.getCreatedBy());
			responseDTO.setCreatedDate(user.getCreatedDate());
			responseDTO.setUpdatedBy(user.getUpdatedBy());
			responseDTO.setUpdatedDate(user.getUpdatedDate());
			
			ResponseStructure<UserResponseDTO> response=new ResponseStructure<>();
			response.setData(responseDTO);
			response.setStatusCode(HttpStatus.CREATED.value());
			response.setMessage("User Data Saved Successfully!!!!!");
			
			return response;
	
	}


}
