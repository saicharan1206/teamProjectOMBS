package com.jspiders.ombs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jspiders.ombs.dto.UserRequestDTO;
import com.jspiders.ombs.entity.User_Role;

public interface UserRoleRepository extends JpaRepository<User_Role, Integer>{

	@Query("select u from User_Role u where u.userRole=?1")
	public User_Role getUserRoleByRole(String userRole);
}
