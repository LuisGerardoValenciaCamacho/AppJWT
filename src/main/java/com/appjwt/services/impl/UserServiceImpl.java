package com.appjwt.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appjwt.entities.Role;
import com.appjwt.entities.User;
import com.appjwt.repositories.RoleRepository;
import com.appjwt.repositories.UserRepository;
import com.appjwt.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository repository;
	
	private RoleRepository roleRepository;
	
	private PasswordEncoder passwordEncoder; 
	
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.repository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return (List<User>) repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findById(long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public User save(User user) {
		Optional<Role> optionalRoleUser = roleRepository.findByClave("ROLE_USER");
		List<Role> roles = new ArrayList<>();
		optionalRoleUser.ifPresent(roles::add);
		if(user.isAdmin()) {
			Optional<Role> optionalRoleAdmin = roleRepository.findByClave("ROLE_ADMIN");
			optionalRoleAdmin.ifPresent(roles::add);
		}
		user.setRoles(roles);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repository.save(user);
	}

	@Override
	@Transactional
	public User update(long id, User user) {
		return null;
	}

	@Override
	@Transactional
	public void delete(long id) {
		repository.deleteById(id); 
	}

	@Override
	public boolean existByUsername(String username) {
		return repository.existsByUsername(username);
	}

}
