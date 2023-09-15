package com.jspiders.ombs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
	
	
	@Query("select u from User u")
	public List<User> getAllUserDetails();
}
