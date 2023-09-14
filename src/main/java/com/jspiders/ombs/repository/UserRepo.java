package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import com.jspiders.ombs.util.ResponseStructure;


public interface UserRepo extends JpaRepository<User, Integer> {
	
	@Query("from UserRole u where u.role=?1")
	UserRole fetchRole(String role);
	
	public User findByEmail(String email);

	public UserRole save(UserRole role);

}
