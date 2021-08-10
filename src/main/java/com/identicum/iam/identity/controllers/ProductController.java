package com.identicum.iam.identity.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.identicum.iam.identity.models.Product;
import com.identicum.iam.identity.repo.ProductRepository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController
{

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ProductRepository productRepository;
	
	@GetMapping()
	public Iterable<Product> index()
	{
		log.debug("Searching products");
		return this.productRepository.findAll();
	}

	@PostMapping()
	public ResponseEntity<Product> create(@RequestBody Product product) {
		log.debug("Creating product {}" , product.getName());

		Product created = this.productRepository.save(product);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(created.getId())
				.toUri();

		return ResponseEntity.created(location).body(created);
	}

	@PreAuthorize("hasAnyRole('delete.product','admin')")
	@DeleteMapping("{id}")
	public void delete(@PathVariable long id) {
		log.debug("Deleting product id {}", id );
		this.productRepository.deleteById(id);
	}

}
