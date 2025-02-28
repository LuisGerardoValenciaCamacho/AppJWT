package com.appjwt.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appjwt.entities.User;
import com.appjwt.services.impl.UserServiceImpl;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http:localhost:8080")
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private UserServiceImpl service;
	
	public UserController(UserServiceImpl userService) {
		this.service = userService;
	}
	
	@GetMapping()
	public ResponseEntity<List<User>> getAllTest() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}
	
	@PostMapping()
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> addTest(@Valid @RequestBody User user, BindingResult result) {
		if(result.hasFieldErrors()) {
			return validation(result);
		}
		User newUser = service.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<User> get(@PathVariable("id") long id) {
		Optional<User> optinalUser = service.findById(id);
		if(optinalUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(service.findById(id).orElseThrow());	
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> add(@Valid @RequestBody User user, BindingResult result) {
		if(result.hasFieldErrors()) {
			return validation(result);
		}
		User newUser = service.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
		user.setAdmin(false);
		return add(user, result);
	}
	
	private ResponseEntity<?> validation(BindingResult result) {
		Map<String, String> errors = new HashMap<>();
		result.getFieldErrors().forEach(err -> {
			errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

}
