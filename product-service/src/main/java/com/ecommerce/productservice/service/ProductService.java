package com.ecommerce.productservice.service;

import lombok.extern.slf4j.Slf4j;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.request.ProductRequest;
import com.ecommerce.productservice.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription())
				.price(productRequest.getPrice()).build();

		productRepository.save(product);
		log.info("Product {} is saved", product.getId());
	}

	public List<ProductResponse> getAllProducts(){
		List<Product> products = productRepository.findAll(); // Read all the products inside the database
		return products.stream().map(this::mapToProductResponse).toList(); 
		// this::mapToProductResponse ==> map.(product -> mapToProductResponse(mapToProductResponse))
		// Method Reference in same class   --> Lambda method							
	}

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder().id(product.getId()).name(product.getName())
				.description(product.getDescription()).price(product.getPrice()).build();
	}
}
