package com.inventory.system;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.system.controller.ProductController;
import com.inventory.system.model.Product;
import com.inventory.system.service.ProductService;

class ProductControllerTest {

	private MockMvc mockMvc;

	@Mock
	private ProductService productService;

	@InjectMocks
	private ProductController productController;

	private Product product1;
	private Product product2;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

		product1 = new Product(1L, "Laptop", "High performance laptop", 999.99, 10, 5);
		product2 = new Product(2L, "Mouse", "Wireless mouse", 19.99, 3, 5);
	}

	@Test
	void testGetAllProducts() throws Exception {
		when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

		mockMvc.perform(get("/api/products")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Laptop")).andExpect(jsonPath("$[1].name").value("Mouse"));
	}

	@Test
	void testGetProductById() throws Exception {
		when(productService.getProductById(1L)).thenReturn(Optional.of(product1));

		mockMvc.perform(get("/api/products/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Laptop"));
	}

	@Test
	void testGetProductByIdNotFound() throws Exception {
		when(productService.getProductById(99L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/products/99")).andExpect(status().isNotFound());
	}

	@Test
	void testCreateProduct() throws Exception {
		when(productService.saveProduct(any(Product.class))).thenReturn(product1);

		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(product1)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("Laptop"));
	}

	@Test
	void testUpdateProduct() throws Exception {
		when(productService.getProductById(1L)).thenReturn(Optional.of(product1));
		when(productService.saveProduct(any(Product.class))).thenReturn(product1);

		product1.setPrice(899.99);

		mockMvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(product1)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.price").value(899.99));
	}

	@Test
	void testDeleteProduct() throws Exception {
		doNothing().when(productService).deleteProduct(1L);

		mockMvc.perform(delete("/api/products/1")).andExpect(status().isNoContent());
	}

	@Test
	void testGetLowStockProducts() throws Exception {
		when(productService.getLowStockProducts()).thenReturn(Arrays.asList(product2));

		mockMvc.perform(get("/api/products/low-stock")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Mouse"));
	}

	// Helper method to convert object to JSON string
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}