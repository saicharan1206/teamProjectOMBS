package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.ombs.entity.User;


public interface UserRepo extends JpaRepository<User, Integer> {

	public User findByUserEmail(String email);
	
}
