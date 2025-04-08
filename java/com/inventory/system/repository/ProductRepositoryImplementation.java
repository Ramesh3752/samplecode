package com.inventory.system.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.inventory.system.model.Product;

@Repository

public class ProductRepositoryImplementation implements ProductRepository {
	private final ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(1);

	@Override
	public Product save(Product product) {
		if (product.getId() == null) {
			// New product
			product.setId(idGenerator.getAndIncrement());
		}
		products.put(product.getId(), product);
		return product;
	}

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return new ArrayList<>(products.values());
	}

	@Override
	public Optional<Product> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(products.get(id));
	}

	@Override
	public void deleteById(Long id) {
		products.remove(id);

	}

	@Override
	public List<Product> findByQuantityLessThan(int threshold) {
		List<Product> result = new ArrayList<>();
		for (Product product : products.values()) {
			if (product.getQuantity() < threshold) {
				result.add(product);
			}
		}
		return result;

	}

}
