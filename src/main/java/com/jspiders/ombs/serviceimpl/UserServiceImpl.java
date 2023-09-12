package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.util.exception.EmailAlreadyFoundException;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repo;
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest) {
		
		String email = userRequest.getUserEmail().toLowerCase();
		User object = repo.findByUserEmail(email);
		if(object==null) {
			User user = new User();
			user.setUserFirstName(userRequest.getUserFirstName());
			user.setUserLastName(userRequest.getUserLastName());
			user.setUserRole(userRequest.getUserRole());
			user.setUserEmail(email);
			user.setUserPassword(userRequest.getUserPassword());
			
	 /** Because of the EnableJpaAuditing & @EntityListeners(AuditingEntityListener.class) **/
			/** Created Date -- user.setCreatedDate(LocalDateTime.now());**/
			/** Updated Date -- user.setUpdatedDate(LocalDateTime.now());**/
			// UpdatedBy
			
			User user2 = repo.save(user);
			
			UserResponseDTO response = new UserResponseDTO();
			response.setUserEmail(user2.getUserEmail());
			response.setUserId(user2.getUserId());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("User Data inserted Successfully");
			structure.setData(response);
			
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		}
		
		throw new EmailAlreadyFoundException("This Email is not applicable");	
	}
}
