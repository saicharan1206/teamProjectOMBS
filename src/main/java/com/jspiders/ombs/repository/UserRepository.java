package com.jspiders.ombs.repository;
import com.jspiders.ombs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findByUserEmail(String email);
	
}
