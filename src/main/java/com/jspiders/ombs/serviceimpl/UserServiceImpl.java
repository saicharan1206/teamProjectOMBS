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
import com.jspiders.ombs.util.exception.EmailException;
import com.jspiders.ombs.util.exception.EmailNotFoundException;
import com.jspiders.ombs.util.exception.PasswordNotMatchingException;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;
	@Autowired
	private UserRoleRepository roleRepository;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> saveUser(UserRequestDTO userRequest) {

		User user1;
		User user = new User();
//		UserRole userRole = new UserRole();
		if (repo.findByEmail(userRequest.getEmail()) == null) {

			UserRole userRole = roleRepository.getUserRoleByRole(userRequest.getUserRole());
			System.err.println(userRequest.getUserRole());
			if (userRole == null) {
				userRole = new UserRole();
				userRole.setUserRole(userRequest.getUserRole());
				roleRepository.save(userRole);
			}
			user.setEmail(userRequest.getEmail().toLowerCase());
			user.setPassword(userRequest.getPassword());
			user.setFirstname(userRequest.getFirstname());
			user.setLastname(userRequest.getLastname());
			user.setUserrole(userRole);
			repo.save(user);

		} else {

			throw new EmailException("email is already present");
		}

		UserResponseDTO response = new UserResponseDTO();
		response.setUserid(user.getUserid());
		response.setEmail(user.getEmail());

		ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("User Saved Sucessfully");
		structure.setData(response);
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> userlogin(UserRequestDTO userRequest) {
		User user = null;
		user = repo.findByEmail(userRequest.getEmail());
		if (user != null) {
			if (user.getPassword().equals(userRequest.getPassword())) {

				UserResponseDTO response = new UserResponseDTO();
				response.setUserid(user.getUserid());
				response.setFirstname(user.getFirstname());
				response.setLastname(user.getLastname());
				response.setUserrole(user.getUserrole());
				;
				ResponseStructure<UserResponseDTO> structure = new ResponseStructure<UserResponseDTO>();
				structure.setStatusCode(HttpStatus.FOUND.value());
				structure.setMessage("Login Sucessfully");
				structure.setData(response);
				return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.FOUND);

			} else {
				throw new PasswordNotMatchingException("Password is not matching");

			}
		} else {
			throw new EmailNotFoundException("Email is not Found");
		}

	}

}
