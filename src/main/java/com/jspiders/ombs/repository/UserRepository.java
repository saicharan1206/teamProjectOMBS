package com.jspiders.ombs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.ombs.entity.User;

import jakarta.mail.MessagingException;

public interface UserRepository extends JpaRepository<User, Integer>
{
	public User findByUserEmail(String userEmail);
	
 	public User findByUserPassword(String userPassword);

}
