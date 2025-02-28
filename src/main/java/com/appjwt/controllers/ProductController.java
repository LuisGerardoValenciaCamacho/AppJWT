package com.appjwt.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appjwt.entities.Product;
import com.appjwt.services.impl.ProductServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	ProductServiceImpl productServiceImpl;
	
	ProductController(ProductServiceImpl productServiceImpl) {
		this.productServiceImpl = productServiceImpl;
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<List<Product>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(productServiceImpl.findAll());
	}
	
	@GetMapping("/find/{id}")
	public ResponseEntity<Product> findById(@PathVariable("id") long id) {
		Optional<Product> productOptional = productServiceImpl.findById(id);
		if(productOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(productOptional.orElseThrow());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, BindingResult result) {
		if(result.hasFieldErrors()) {
			return validation(result);
		}
		Product newProduct = productServiceImpl.save(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateProduct(@Valid @RequestBody Product product, BindingResult result, @PathVariable("id") long id) {
		if(result.hasFieldErrors()) {
			return validation(result);
		}
		Product newProduct = productServiceImpl.update(id, product);
		return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {
		Optional<Product> productOptional = productServiceImpl.findById(id);
		if(productOptional.isPresent()) {
			productServiceImpl.delete(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	private ResponseEntity<?> validation(BindingResult result) {
		Map<String, String> errors = new HashMap<>();
		result.getFieldErrors().forEach(err -> {
			errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
}
