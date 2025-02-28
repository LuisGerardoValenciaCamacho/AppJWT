package com.appjwt.services;

import java.util.List;
import java.util.Optional;

import com.appjwt.entities.User;

public interface UserService {

	List<User> findAll();
	
	Optional<User> findById(long id);
	
	User save(User user);
	
	User update(long id, User user);
	
	void delete(long id);
	
	boolean existByUsername(String username);
}
