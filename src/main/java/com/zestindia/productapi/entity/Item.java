package com.zestindia.productapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item", indexes = @Index(name = "idx_item_product_id", columnList = "product_id"))
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	Product product;
	@Column(nullable = false)
	Integer quantity;

	public Long getId() {
		return id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer q) {
		quantity = q;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product p) {
		product = p;
	}
}
