package com.jspiders.ombs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	
	@Query(" select u from UserRole u")
	public List<UserRole> getAllUserRoles();
	
	
	@Query("select u.userEmail from User u where userEmail=?1")
	public String getUserByEmail(String userEmail);
	
	

}


