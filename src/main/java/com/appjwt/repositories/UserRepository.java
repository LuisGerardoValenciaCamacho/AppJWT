package com.appjwt.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.appjwt.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	boolean existsByUsername(String username);
	
	Optional<User> findByUsername(String username);
}
