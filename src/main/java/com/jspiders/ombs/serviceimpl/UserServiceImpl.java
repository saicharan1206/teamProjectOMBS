package com.jspiders.ombs.serviceimpl;

import java.util.List;
import java.util.Optional;

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
import com.jspiders.ombs.util.exception.EmailAlreadyExistsException;
import com.jspiders.ombs.util.exception.UserNotFoundByIdException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    UserRepository repo;
	@Override
	
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO request) {
		String email = request.getEmail().toLowerCase();
		User user = new User();
		List<User> list = repo.findAll();
		boolean flag = true;
            if(!list.isEmpty()) {
            	for(int i=0; i<=list.size()-1; i++) {
    				if(list.get(i).getEmail().equalsIgnoreCase(email)) {
    					flag = false;	
    				}
    			}	
            }
            ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
			if(flag == true) {
				user.setUserFirstName(request.getUserFirstName());
				user.setUserLastName(request.getUserLastName());
				user.setUserRole(request.getUserRole());
				user.setEmail(email);
				user.setPassword(request.getPassword());
			
				repo.save(user);
				
				UserResponseDTO response = new UserResponseDTO();
				response.setMessage("Email saved sucessfully");
				
				
				structure.setStatusCode(HttpStatus.CREATED.value());
				structure.setMessage("Email saved sucessfully");
				structure.setData(response);
				
				
			}
			else {
				throw new EmailAlreadyExistsException( email, " User cannot be saved" );
			}

		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.CREATED );
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> updateUser(UserRequestDTO request, int userId) {
		Optional<User> user = repo.findById(userId);
		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<>();
		if(!user.isEmpty())
		{
			User user1 = new User();
			user1.setID(userId);
			user1.setUserFirstName(request.getUserFirstName());
			user1.setUserLastName(request.getUserLastName());
			user1.setUserRole(request.getUserRole());
			user1.setEmail(request.getEmail());
			user1.setPassword(request.getPassword());
			repo.save(user1);
			
			UserResponseDTO response = new UserResponseDTO();
			response.setMessage("Updated sucessfully");
			
			
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setMessage("Data updated successfully");
			structure.setData(response);
	
		}
		else {
			throw new UserNotFoundByIdException(userId,"User data cannot be updated");
		}
		
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK) ;
	}

}
