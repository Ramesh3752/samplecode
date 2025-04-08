package com.inventory.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.inventory.system.model.Product;

@Repository
public interface ProductRepository {
	Product save(Product product);

	List<Product> findAll();

	Optional<Product> findById(Long id);

	void deleteById(Long id);

	List<Product> findByQuantityLessThan(int threshold);

}
