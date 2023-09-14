package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import com.jspiders.ombs.dto.UserResponseDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.util.ResponseStructure;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUserEmail(String userEmail);

}
