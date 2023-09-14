package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findByUserEmail(String userEmail);
	
	
}
