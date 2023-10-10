package com.jspiders.ombs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;

import org.springframework.transaction.annotation.Transactional;


public interface UserRepo extends JpaRepository<User, Integer> {
	
	@Query("from UserRole u where u.role=?1")
	UserRole fetchRole(String role);
	
	public User findByEmail(String email);

	public List<User> findByUserRole(UserRole role);
	
}
