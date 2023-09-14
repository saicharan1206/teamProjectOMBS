package com.jspiders.ombs.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.Role;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.util.exception.EmailAlreadyFoundException;
import com.jspiders.ombs.util.exception.RoleNotApplicableException;
import com.jspiders.ombs.util.exception.UserNotFoundByEmailException;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.UserRoleRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserRoleRepository roleRepo;
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest) {
		
		String email = userRequest.getUserEmail().toLowerCase();
		String roleName = userRequest.getUserRole().toLowerCase();
		
		User object = userRepo.findByUserEmail(email);
		
		Role userRole = new Role();
		
		if(object==null){
		
			userRole = roleRepo.findByRoleName(roleName);
			
			if(userRole == null) {
				userRole = new Role();
				userRole.setRoleName(roleName);
				roleRepo.save(userRole);
			}
				
				User user = new User();
				user.setUserFirstName(userRequest.getUserFirstName());
				user.setUserLastName(userRequest.getUserLastName());
				user.setUserRole(userRole);
				user.setUserEmail(email);
				user.setUserPassword(userRequest.getUserPassword());	
				
		 /** Because of the EnableJpaAuditing & @EntityListeners(AuditingEntityListener.class) **/
				/** Created Date -- user.setCreatedDate(LocalDateTime.now());**/
				/** Updated Date -- user.setUpdatedDate(LocalDateTime.now());**/
				// UpdatedBy
				
				Role role = roleRepo.save(userRole);
				User user2 = userRepo.save(user);
							
				UserResponseDTO response = new UserResponseDTO();
				response.setUserId(user2.getUserId());
				response.setUserFirstName(user2.getUserFirstName());
				response.getUserRole(role.getRoleName());
				
				ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
				structure.setStatusCode(HttpStatus.CREATED.value());
				structure.setMessage("User Data inserted Successfully");
				structure.setData(response);
				
				return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
			}
		throw new EmailAlreadyFoundException("This Email is not applicable");	
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> logInUser(String email, String password) {

		User user = userRepo.findByUserEmail(email.toLowerCase());		
		ResponseStructure<String> structure = new ResponseStructure<String>();
		
		if(user != null) {
			
			/** If user email is Correct **/
			System.out.println(email + " " + password);
			System.out.println(user.getUserEmail() + " " + user.getUserPassword());
			
			String pword = user.getUserPassword();
			
			if(password.equals(pword)) {
				
				/** password == pword (Compares address).. 
				 * 	password.equals(pword) (Compares Values)
				 */
				
				/** If user password is Correct **/
				System.out.println("Password is Correct");
				
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Successfully !!");
				structure.setData("Welcome Home, " +user.getUserRole().getRoleName());
				
				return new ResponseEntity<ResponseStructure<String>>(structure , HttpStatus.FOUND);
			}
			
			/** If user password is inCorrect **/
			System.out.println("Password is InCorrect");
			
			structure.setStatusCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Sorry!! Please Enter Valid Password");
			structure.setData("No User Found for this Credentials");
			
			return new ResponseEntity<ResponseStructure<String>>(structure , HttpStatus.NOT_FOUND);
		}
		/** If user email is inCorrect **/
		throw new UserNotFoundByEmailException("Invalid Email !!");
	}
}
