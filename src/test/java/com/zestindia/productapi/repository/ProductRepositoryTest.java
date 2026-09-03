package com.zestindia.productapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.zestindia.productapi.entity.Product;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;


    // 1. SAVE
    @Test
    void save_shouldSaveProduct() {

        Product product = new Product();
        product.setProductName("Laptop");
        product.setCreatedBy("Swapnil");

        Product saved = productRepository.save(product);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getProductName()).isEqualTo("Laptop");
        assertThat(saved.getCreatedBy()).isEqualTo("Swapnil");
    }


    // 2. FIND BY ID - Product exists
    @Test
    void findById_shouldReturnProduct() {

        Product product = new Product();
        product.setProductName("Mobile");
        product.setCreatedBy("Swapnil");

        Product saved = productRepository.save(product);

        Optional<Product> result =
                productRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getProductName())
                .isEqualTo("Mobile");
    }


    // 3. FIND BY ID - Product does not exist
    @Test
    void findById_shouldReturnEmpty_whenProductNotFound() {

        Optional<Product> result =
                productRepository.findById(999L);

        assertThat(result).isEmpty();
    }


    // 4. FIND ALL
    @Test
    void findAll_shouldReturnProducts() {

        Product product1 = new Product();
        product1.setProductName("Laptop");
        product1.setCreatedBy("Swapnil");

        Product product2 = new Product();
        product2.setProductName("Mobile");
        product2.setCreatedBy("Swapnil");

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products =
                productRepository.findAll();

        assertThat(products).hasSize(2);
        assertThat(products)
                .extracting(Product::getProductName)
                .containsExactlyInAnyOrder("Laptop", "Mobile");
    }


    // 5. EXISTS BY ID - Product exists
    @Test
    void existsById_shouldReturnTrue_whenProductExists() {

        Product product = new Product();
        product.setProductName("Tablet");
        product.setCreatedBy("Swapnil");

        Product saved = productRepository.save(product);

        boolean exists =
                productRepository.existsById(saved.getId());

        assertThat(exists).isTrue();
    }


    // 6. EXISTS BY ID - Product does not exist
    @Test
    void existsById_shouldReturnFalse_whenProductNotFound() {

        boolean exists =
                productRepository.existsById(999L);

        assertThat(exists).isFalse();
    }


    // 7. DELETE BY ID
    @Test
    void deleteById_shouldDeleteProduct() {

        Product product = new Product();
        product.setProductName("Keyboard");
        product.setCreatedBy("Swapnil");

        Product saved = productRepository.save(product);

        Long id = saved.getId();

        productRepository.deleteById(id);

        Optional<Product> result =
                productRepository.findById(id);

        assertThat(result).isEmpty();
    }
}