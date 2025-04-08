package com.inventory.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI inventoryOpenAPI() {
		return new OpenAPI().info(new Info().title("Inventory System API")
				.description("API for managing products in inventory").version("1.0"));
	}
}
