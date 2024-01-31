package com.cyperts.ExcellML.UserAndRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsernameOrEmail(String username, String email);

	Object existsByUsername(String username);

	Object existsByEmail(String email);

	boolean existsById(long userId);
	
	User findByUsername(String username);

	User findByEmail(String email);

}
