package com.jspiders.ombs.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.repository.UserRepository;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;

	@Override
	public ResponseEntity<ResponseStructure<User>> saveUser(UserRequestDTO userRequestDTO) {
		User user = new User();
		user.setEmailAddress(userRequestDTO.getEmailaddress());
		user.setPassword(userRequestDTO.getPassword());
		user.setCreatedBy("Deepak");
		user.setCreatedDate(LocalDateTime.now());
		User user2 = repo.save(user);

		ResponseStructure<User> structure = new ResponseStructure<User>();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Student Data Saved Successfully!!");
		structure.setData(user2);
		return new ResponseEntity<ResponseStructure<User>>(structure, HttpStatus.CREATED);
	}
}
