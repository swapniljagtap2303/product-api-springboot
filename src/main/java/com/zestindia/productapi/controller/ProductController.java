package com.zestindia.productapi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.zestindia.productapi.dto.DTOs.ItemRequest;
import com.zestindia.productapi.dto.DTOs.ItemResponse;
import com.zestindia.productapi.dto.DTOs.ProductRequest;
import com.zestindia.productapi.dto.DTOs.ProductResponse;
import com.zestindia.productapi.service.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
	final ProductService s;

	public ProductController(ProductService x) {
		s = x;
	}

	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest r) {
		return ResponseEntity.status(201).body(s.create(r));
	}

	@GetMapping
	public Page<ProductResponse> all(@PageableDefault(size = 10, sort = "id") Pageable p) {
		return s.all(p);
	}

	@GetMapping("/{id}")
	public ProductResponse get(@PathVariable Long id) {
		return s.get(id);
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest r) {
		return s.update(id, r);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void del(@PathVariable Long id) {
		s.delete(id);
	}

	@PostMapping("/{id}/items")
	public ResponseEntity<ItemResponse> addItem(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(s.addItem(id, request));
	}

	@GetMapping("/{id}/items")
	public List<ItemResponse> getItems(@PathVariable Long id) {
		return s.getItems(id);
	}

	@GetMapping("/{id}/items/{itemId}")
	public ItemResponse getItem(@PathVariable Long id, @PathVariable Long itemId) {

		return s.getItem(id, itemId);
	}

	@PutMapping("/{id}/items/{itemId}")
	public ItemResponse updateItem(@PathVariable Long id, @PathVariable Long itemId,
			@Valid @RequestBody ItemRequest request) {

		return s.updateItem(id, itemId, request);
	}

	@DeleteMapping("/{id}/items/{itemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteItem(@PathVariable Long id, @PathVariable Long itemId) {

		s.deleteItem(id, itemId);
	}

}
