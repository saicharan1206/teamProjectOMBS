package com.jspiders.ombs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;


public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("select r.email from User r where email=?1")
	public String getUserEmailByEmail(String email);
	
	@Query("select r from User r where email=?1")
	public User getUserByEmail(String email);
	
	


}
