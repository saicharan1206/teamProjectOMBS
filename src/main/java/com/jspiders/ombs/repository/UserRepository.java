package com.jspiders.ombs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jspiders.ombs.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u")
	public List<User> getAllUsers();
	
	@Query("selectt u.userEmail from User u where userEmail=?1")
	public User findUserByEmail(String userEmail);

	@Query("selectt u.userEmail, u.userPassword from User u where userEmail=?1 and userPassword=?2")
    public List<String> getEmailAndPassword(String userEmail,String userPassword);

	public String getUserByEmail(String userEmail);
}
