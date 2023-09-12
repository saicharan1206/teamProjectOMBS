package com.jspiders.ombs.service;

import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.util.ResponseStructure;



public interface UserService {
	public ResponseEntity<ResponseStructure<User>> saveUser(UserRequestDTO userRequestDTO);
}
