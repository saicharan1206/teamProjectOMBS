package com.jspiders.ombs.serviceimpl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.User_Role;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.repository.User_Role_Repository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;
import com.jspiders.ombs.util.exception.EmailAlreadyExistException;
import com.jspiders.ombs.util.exception.UserNotFoundByEmailException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private User_Role_Repository user_Role_Repository;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequestDTO) {
		String email=userRequestDTO.getUserEmail().toLowerCase();
		
		User user1 = userRepo.findByUserEmail(email);
		
		User_Role userRole = new User_Role();
		
		if (user1==null) {
			
			System.out.println(userRole+"-ssdfgnhj");
			userRole = user_Role_Repository.findByUserRoleName(userRequestDTO.getUserRole());
			System.out.println(userRole+"dfvghnjkml");
			
			if (userRole==null) {
			userRole = new User_Role();
			System.out.println(userRequestDTO.getUserRole());
			userRole.setUserRoleName(userRequestDTO.getUserRole());
			System.out.println(userRole+"111111111");
			user_Role_Repository.save(userRole);
			}
			
			User user = new User();
			
			user.setUserEmail(email);
			user.setUserPassword(userRequestDTO.getUserPassword());
			user.setUserFirstName(userRequestDTO.getUserFirstName());
			user.setUserLastName(userRequestDTO.getUserLastName());
			
			user.setUserRole(userRole); 
			
			userRepo.save(user);
			
			UserResponseDTO respone = new UserResponseDTO();
			respone.setUserId(user.getUserId());
			respone.setUserEmail(user.getUserEmail());
			respone.setUserFirstName(user.getUserFirstName());
			respone.setUserLastName(user.getUserLastName());
			
			ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("User data inserted Successfully!!!");
			structure.setData(respone);
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure,HttpStatus.CREATED);
		}
		else
			throw new EmailAlreadyExistException(email+" Email already exists!!!");
			
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> userLogin(String userEmail, String userPassword) {
//		userEmail.toLowerCase();
		User user = userRepo.findByUserEmail(userEmail.toLowerCase());
		System.out.println(user);
		
		ResponseStructure<String> structure = new ResponseStructure<String>();
		
		/** here we are trying to fetch data from user object, but if user not found means it is null &
		 * from null we cannot fetch any data, it's an Runtime error  */
//		System.out.println(user.getUserId()+" "+user.getUserEmail()); 
		if (user!=null) {
			String pwd = user.getUserPassword();
			System.out.println("Password:----"+pwd);
			System.out.println(pwd+"=="+userPassword);
			System.out.println(pwd==userPassword+"######");
			/** the above statement gives false because == compares address not String password, so we need to use equals() method */
			if (pwd.equals(userPassword)) {
				System.out.println("Password:++++ "+pwd);
				String roleName = user.getUserRole().getUserRoleName();
				System.out.println("User role: "+roleName);
				
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Successfull!!!");
				structure.setData("Welcome Home: "+roleName);
				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.FOUND);
			}
			else {
				structure = new ResponseStructure<String>();
				structure.setStatusCode(HttpStatus.NOT_FOUND.value());
				structure.setMessage("Password is Incorrect !!!");
				structure.setData("Login Failed !!!!");
				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.NOT_FOUND);
				/** throw new InvalidPasswordException */
			}
		}
		else {
			throw new UserNotFoundByEmailException(userEmail+" This doesnot exist, Please create Account!!!");
		}
		
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userLogin2(UserRequestDTO userRequestDTO) {
		String userEmail = userRequestDTO.getUserEmail().toLowerCase();
		User user = userRepo.findByUserEmail(userEmail);
		return null;
	}

	
}

