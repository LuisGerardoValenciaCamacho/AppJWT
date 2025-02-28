package com.appjwt.services;

import java.util.List;
import java.util.Optional;

import com.appjwt.entities.Product;


public interface ProductService {

	List<Product> findAll();
	
	Optional<Product> findById(long id);
	
	Product save(Product product);
	
	Product update(long id, Product product);
	
	void delete(long id);
}
