package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import com.jspiders.ombs.entity.User_Role;

public interface User_Role_Repository extends JpaRepository<User_Role, Integer> {
//	@Query("select u from User_Role u where u.userRoleName=?1")
	public User_Role findByUserRoleName(String roleName);

}
