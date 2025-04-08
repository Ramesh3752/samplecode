package com.inventory.system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.system.model.Product;
import com.inventory.system.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController // This marks the class as restful controller and allows the class to handle
				// HTTP requests and return JSON responses.
@RequestMapping("/api/products") // specifies that all API endpoints in this controller starts with
									// /api/products.
@Tag(name = "Product Controller", description = "APIs for product management") // Describing the purpose of the
																				// controller.
public class ProductController { // class named as Productcontroller
	@Autowired // Injects an instance of productservice
	private ProductService productService; // declaration

	@Operation(summary = "Create a new product", description = "Adds a new product to inventory") // describes the
																									// endpoint

	// @PostMapping // maps http post requests to this method
	// public ResponseEntity<Product> createProduct(@RequestBody Product product) {
	// Product savedProduct = productService.saveProduct(product); // save the
	// product
	// return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);// returns the
	// saved product with
																		// httpstatus.created(201)
	// }

	@PostMapping("/create")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		Product savedProduct = productService.saveProduct(product);
		return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
	}

	@PostMapping("/another")
	public ResponseEntity<String> anotherPostMethod() {
		return new ResponseEntity<>("Another POST method", HttpStatus.CREATED);
	}


	@Operation(summary= "collects all the products") // describes the
																									// endpoint
	@GetMapping // Map http get requests to this method
    public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> products = productService.getAllProducts();// to fetch all products
		return new ResponseEntity<>(products, HttpStatus.OK); // returns the list of products with httpstatus.ok(200)
    }

	@Operation(summary= "gets  product by id") // describes the
																									// endpoint
	@GetMapping("/{id}") // Allow requests like /api/products/1
	public ResponseEntity<Product> getProductById(@PathVariable Long id) { // @pathvariable long id: will extracts id
																			// from url
		Optional<Product> product = productService.getProductById(id); // fetches the product
		return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // products exits then it will return
																				// httpsstatus.ok(200)
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // products doesn't exits then it will
																				// return httpsstatus.notfound(404)
    }

	@Operation(summary = "Updates product information by product id") // describes the
																									// endpoint
	@PutMapping("/{id}") // Maps HTTP PUT requests to update an existing product.

    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            Product existingProduct = product.get();
            existingProduct.setName(productDetails.getName());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setQuantity(productDetails.getQuantity());
            existingProduct.setReorderThreshold(productDetails.getReorderThreshold());

            Product updatedProduct = productService.saveProduct(existingProduct);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

	@Operation(summary = "Deletes product information by product id") //// describes the
																									//// endpoint
	@DeleteMapping("/{id}") // Maps HTTP DELETE requests.
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@Operation(summary = "Returns the lowStck products")

	@GetMapping("/low-stock") // Handles requests like /api/products/low-stock.
    public ResponseEntity<List<Product>> getLowStockProducts() {
        List<Product> lowStockProducts = productService.getLowStockProducts();
        return new ResponseEntity<>(lowStockProducts, HttpStatus.OK);
    }
}