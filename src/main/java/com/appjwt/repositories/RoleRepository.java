package com.appjwt.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.appjwt.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByName(String name);
	
	Optional<Role> findByClave(String clave);
}
