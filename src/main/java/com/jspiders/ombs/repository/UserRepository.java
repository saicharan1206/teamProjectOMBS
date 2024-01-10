package com.jspiders.ombs.repository;
import com.jspiders.ombs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findByUserEmail(String email);
	
	@Query("select u.userEmail from User u where userEmail=?1")
	public String getUserEmailByEmail(String userEmail);
	
}
