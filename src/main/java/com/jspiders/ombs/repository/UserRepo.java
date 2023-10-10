package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jspiders.ombs.entity.User;
import com.jspiders.ombs.entity.UserRole;
import java.util.List;




public interface UserRepo extends JpaRepository<User, Integer> {
	
	
	@Query("from UserRole u where u.role = ?1")
	public UserRole fetchRole(String role) ;
	
	public  User findByEmailId(String emailId); 
	
}
