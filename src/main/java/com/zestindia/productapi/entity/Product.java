package com.zestindia.productapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "product", indexes = @Index(name = "idx_product_name", columnList = "product_name"))
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(name = "product_name", nullable = false)
	String productName;
	@Column(name = "created_by", nullable = false)
	String createdBy;
	@Column(name = "created_on", nullable = false)
	LocalDateTime createdOn;
	@Column(name = "modified_by")
	String modifiedBy;
	@Column(name = "modified_on")
	LocalDateTime modifiedOn;
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Item> items = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String x) {
		productName = x;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String x) {
		createdBy = x;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String x) {
		modifiedBy = x;
	}

	public List<Item> getItems() {
		return items;
	}

	@PrePersist
	void c() {
		createdOn = LocalDateTime.now();
	}

	@PreUpdate
	void u() {
		modifiedOn = LocalDateTime.now();
	}
}
