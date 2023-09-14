package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.ombs.entity.User_Role;

public interface User_RoleRepository extends JpaRepository<User_Role, Integer> {
	
	public User_Role findByUserRole (String userRole);
	
}
