package com.zestindia.productapi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zestindia.productapi.dto.DTOs.ItemRequest;
import com.zestindia.productapi.dto.DTOs.ItemResponse;
import com.zestindia.productapi.dto.DTOs.ProductRequest;
import com.zestindia.productapi.dto.DTOs.ProductResponse;
import com.zestindia.productapi.entity.Item;
import com.zestindia.productapi.entity.Product;
import com.zestindia.productapi.exception.ResourceNotFoundException;
import com.zestindia.productapi.repository.ItemRepository;
import com.zestindia.productapi.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	final ProductRepository repo;

	private final ItemRepository itemRepo;

	public ProductServiceImpl(ProductRepository repo, ItemRepository itemRepo) {
		this.repo = repo;
		this.itemRepo = itemRepo;
	}

	ProductResponse map(Product p) {
		return new ProductResponse(p.getId(), p.getProductName(), p.getCreatedBy(), p.getCreatedOn(), p.getModifiedBy(),
				p.getModifiedOn());
	}

	private ItemResponse mapItem(Item item) {

		return new ItemResponse(item.getId(), item.getProduct().getId(), item.getQuantity());
	}

	public ProductResponse create(ProductRequest r) {
		Product p = new Product();
		p.setProductName(r.productName());
		p.setCreatedBy(r.createdBy());
		return map(repo.save(p));
	}

	public ProductResponse get(Long id) {
		return map(repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id)));
	}

	public Page<ProductResponse> all(Pageable p) {
		return repo.findAll(p).map(this::map);
	}

	public ProductResponse update(Long id, ProductRequest r) {
		Product p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
		p.setProductName(r.productName());
		p.setModifiedBy(r.createdBy());
		return map(repo.save(p));
	}

	public void delete(Long id) {
		if (!repo.existsById(id))
			throw new ResourceNotFoundException("Product not found: " + id);
		repo.deleteById(id);
	}

	@Override
	public ItemResponse addItem(Long productId, ItemRequest request) {

		Product product = repo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

		Item item = new Item();

		item.setProduct(product);
		item.setQuantity(request.quantity());

		return mapItem(itemRepo.save(item));
	}

	@Override
	public List<ItemResponse> getItems(Long productId) {

	    Product product = repo.findById(productId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Product not found: " + productId));

	    return product.getItems()
	            .stream()
	            .map(this::mapItem)
	            .toList();
	}

	@Override
	public ItemResponse getItem(Long productId, Long itemId) {

		Item item = itemRepo.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));

		if (!item.getProduct().getId().equals(productId)) {

			throw new ResourceNotFoundException("Item not found for product: " + productId);
		}

		return mapItem(item);
	}

	@Override
	public ItemResponse updateItem(Long productId, Long itemId, ItemRequest request) {

		Item item = itemRepo.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));

		if (!item.getProduct().getId().equals(productId)) {

			throw new ResourceNotFoundException("Item not found for product: " + productId);
		}

		item.setQuantity(request.quantity());

		return mapItem(itemRepo.save(item));
	}

	@Override
	public void deleteItem(Long productId, Long itemId) {

		Item item = itemRepo.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));

		if (!item.getProduct().getId().equals(productId)) {

			throw new ResourceNotFoundException("Item not found for product: " + productId);
		}

		itemRepo.delete(item);
	}

}
