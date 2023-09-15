package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.ombs.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmailAddress(String email);
	// public User forgotPassword(String password);

}
