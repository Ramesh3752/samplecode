package com.inventory.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.inventory.system.model.Product;
import com.inventory.system.repository.ProductRepository;
import com.inventory.system.service.ProductService;

class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private Product product1;
	private Product product2;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		product1 = new Product(1L, "Laptop", "High performance laptop", 999.99, 10, 5);
		product2 = new Product(2L, "Mouse", "Wireless mouse", 19.99, 3, 5);
	}

	@Test
	void testGetAllProducts() {
		when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

		List<Product> products = productService.getAllProducts();

		assertEquals(2, products.size());
		verify(productRepository, times(1)).findAll();
	}

	@Test
	void testGetProductById() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

		Optional<Product> foundProduct = productService.getProductById(1L);

		assertTrue(foundProduct.isPresent());
		assertEquals("Laptop", foundProduct.get().getName());
	}

	@Test
	void testSaveProduct() {
		when(productRepository.save(any(Product.class))).thenReturn(product1);

		Product savedProduct = productService.saveProduct(product1);

		assertNotNull(savedProduct);
		assertEquals(1L, savedProduct.getId());
	}

	@Test
	void testDeleteProduct() {
		doNothing().when(productRepository).deleteById(1L);

		productService.deleteProduct(1L);

		verify(productRepository, times(1)).deleteById(1L);
	}

	@Test
	void testGetLowStockProducts() {
		Product lowStockProduct = new Product(3L, "Keyboard", "Mechanical", 89.99, 2, 5);
		when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2, lowStockProduct));

		List<Product> lowStockProducts = productService.getLowStockProducts();

		assertEquals(2, lowStockProducts.size()); // Mouse and Keyboard are low stock
	}
}
