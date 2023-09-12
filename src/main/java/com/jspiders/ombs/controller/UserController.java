package com.jspiders.ombs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.service.UserService;
import com.jspiders.ombs.util.ResponseStructure;



/*
 *  @Validated annotation is used to validate the request parameters before accepting
 */

@RestController
@Validated
@CrossOrigin
public class UserController {

	/*
	 *  @Autowired is used to inject dependency automatically
	 */
	@Autowired
	private UserService userService;
	
	/*
	 *  This webservice is accepting user details from UI and returning response inside 
	 *  responsestructure object
	 */
	@PostMapping("user/add")
	public ResponseEntity<ResponseStructure> addUser(@RequestBody @Validated UserRequestDTO requestDTO) {
		return userService.addUser(requestDTO);
	}
}
