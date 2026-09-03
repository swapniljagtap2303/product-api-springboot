package com.zestindia.productapi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zestindia.productapi.dto.DTOs.ItemRequest;
import com.zestindia.productapi.dto.DTOs.ItemResponse;
import com.zestindia.productapi.dto.DTOs.ProductRequest;
import com.zestindia.productapi.dto.DTOs.ProductResponse;

public interface ProductService {
	ProductResponse create(ProductRequest r);

	ProductResponse get(Long id);

	Page<ProductResponse> all(Pageable p);

	ProductResponse update(Long id, ProductRequest r);

	void delete(Long id);
	
	ItemResponse addItem(Long productId, ItemRequest request);

    List<ItemResponse> getItems(Long productId);

    ItemResponse getItem(Long productId, Long itemId);

    ItemResponse updateItem(Long productId, Long itemId, ItemRequest request);

    void deleteItem(Long productId, Long itemId);
}
