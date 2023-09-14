package com.jspiders.ombs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.ombs.entity.UserRole;

public interface UserRoleRepo extends JpaRepository<UserRole, Integer>
{
    public UserRole findByUserRoleName(String userRoleName);
}
