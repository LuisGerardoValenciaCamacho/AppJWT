package com.appjwt.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appjwt.entities.Product;
import com.appjwt.repositories.ProductRepository;
import com.appjwt.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	ProductRepository repository;
	
	public ProductServiceImpl(ProductRepository productRepository) {
		this.repository = productRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll() {
		return (List<Product>) repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Product> findById(long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public Product save(Product product) {
		return repository.save(product);
	}
	
	@Override
	@Transactional
	public Product update(long id, Product product) {
		Optional<Product> optionalProducto = repository.findById(id);
		if(optionalProducto.isPresent()) {
			Product item = optionalProducto.orElseThrow();
			item.setName(product.getName());
			item.setPrice(product.getPrice());
			item.setDescription(product.getDescription());
			return repository.save(item);
		}
		return optionalProducto.orElseThrow();
	}

	@Override
	@Transactional
	public void delete(long id) {
		Product product = repository.findById(id).orElseThrow();
		repository.delete(product);
	}

}
