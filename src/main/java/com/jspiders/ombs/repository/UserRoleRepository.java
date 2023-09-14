package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jspiders.ombs.entity.UserRole;

public interface UserRoleRepository  extends JpaRepository<UserRole, Integer>{
	@Query("select u from UserRole u where u.userRole=?1")
	public  UserRole getUserRoleByRole(String userRole);
}
