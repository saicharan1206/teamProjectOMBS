package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jspiders.ombs.entity.Role;

public interface UserRoleRepository extends JpaRepository<Role, Integer>{

	public Role findByRoleName(String roleName);
	
}
