package com.jspiders.ombs.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jspiders.ombs.entity.UserRole;

public interface UserRoleRepositary extends JpaRepository<UserRole, Integer>  {
	
	@Query("Select u from UserRole u where u.userRole=?1")
	public UserRole getUserRoleByRole(String userRole);

}
