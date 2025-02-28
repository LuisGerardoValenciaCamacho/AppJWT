package com.appjwt.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appjwt.entities.User;
import com.appjwt.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {
	
	private UserRepository repositry;
	
	public JpaUserDetailsService(UserRepository userRepository) {
		this.repositry = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = repositry.findByUsername(username);
		if(!userOptional.isPresent()) {
			throw new UsernameNotFoundException("No existe el usuario");
		}
		User user = userOptional.orElseThrow();
		List<GrantedAuthority> authorities = user.getRoles().stream()
			.map(role -> new SimpleGrantedAuthority(role.getClave()))
			.collect(Collectors.toList());
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), 
			user.getPassword(), 
			user.isEnabled(), 
			true, 
			true, 
			true, 
			authorities
		);
	}
}
