package com.zestindia.productapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.zestindia.productapi.dto.DTOs.ItemRequest;
import com.zestindia.productapi.dto.DTOs.ItemResponse;
import com.zestindia.productapi.dto.DTOs.ProductRequest;
import com.zestindia.productapi.dto.DTOs.ProductResponse;
import com.zestindia.productapi.entity.Item;
import com.zestindia.productapi.entity.Product;
import com.zestindia.productapi.exception.ResourceNotFoundException;
import com.zestindia.productapi.repository.ItemRepository;
import com.zestindia.productapi.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository repo;

    @Mock
    ItemRepository itemRepo;

    @InjectMocks
    ProductServiceImpl service;


    // =========================================================
    // 1. CREATE PRODUCT
    // =========================================================

    @Test
    void create_shouldReturnProductResponse() {

        ProductRequest request =
                new ProductRequest("Laptop", "Swapnil");

        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 1L);

        product.setProductName("Laptop");
        product.setCreatedBy("Swapnil");

        when(repo.save(any(Product.class)))
                .thenReturn(product);

        ProductResponse result =
                service.create(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Laptop", result.productName());
        assertEquals("Swapnil", result.createdBy());

        verify(repo).save(any(Product.class));
    }


    // =========================================================
    // 2. GET PRODUCT - SUCCESS
    // =========================================================

    @Test
    void get_shouldReturnProductResponse_whenProductExists() {

        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 1L);

        product.setProductName("Laptop");
        product.setCreatedBy("Swapnil");

        when(repo.findById(1L))
                .thenReturn(Optional.of(product));

        ProductResponse result =
                service.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Laptop", result.productName());
        assertEquals("Swapnil", result.createdBy());

        verify(repo).findById(1L);
    }


    // =========================================================
    // 3. GET PRODUCT - NOT FOUND
    // =========================================================

    @Test
    void get_shouldThrowException_whenProductNotFound() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.get(1L)
        );

        verify(repo).findById(1L);
    }


    // =========================================================
    // 4. GET ALL PRODUCTS
    // =========================================================

    @Test
    void all_shouldReturnProductPage() {

        Product product1 = new Product();

        ReflectionTestUtils.setField(product1, "id", 1L);

        product1.setProductName("Laptop");
        product1.setCreatedBy("Swapnil");


        Product product2 = new Product();

        ReflectionTestUtils.setField(product2, "id", 2L);

        product2.setProductName("Mobile");
        product2.setCreatedBy("Swapnil");


        List<Product> products =
                List.of(product1, product2);

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Product> productPage =
                new PageImpl<>(products);

        when(repo.findAll(pageable))
                .thenReturn(productPage);

        Page<ProductResponse> result =
                service.all(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        assertEquals(
                "Laptop",
                result.getContent().get(0).productName()
        );

        assertEquals(
                "Mobile",
                result.getContent().get(1).productName()
        );

        verify(repo).findAll(pageable);
    }


    // =========================================================
    // 5. UPDATE PRODUCT - SUCCESS
    // =========================================================

    @Test
    void update_shouldReturnUpdatedProduct() {

        ProductRequest request =
                new ProductRequest("Gaming Laptop", "Swapnil");


        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 1L);

        product.setProductName("Laptop");
        product.setCreatedBy("Swapnil");


        when(repo.findById(1L))
                .thenReturn(Optional.of(product));

        when(repo.save(any(Product.class)))
                .thenReturn(product);


        ProductResponse result =
                service.update(1L, request);


        assertNotNull(result);

        assertEquals(
                1L,
                result.id()
        );

        assertEquals(
                "Gaming Laptop",
                result.productName()
        );

        assertEquals(
                "Swapnil",
                result.modifiedBy()
        );

        verify(repo).findById(1L);
        verify(repo).save(product);
    }


    // =========================================================
    // 6. UPDATE PRODUCT - NOT FOUND
    // =========================================================

    @Test
    void update_shouldThrowException_whenProductNotFound() {

        ProductRequest request =
                new ProductRequest("Gaming Laptop", "Swapnil");

        when(repo.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.update(1L, request)
        );


        verify(repo).findById(1L);

        verify(repo, never())
                .save(any(Product.class));
    }


    // =========================================================
    // 7. DELETE PRODUCT - SUCCESS
    // =========================================================

    @Test
    void delete_shouldDeleteProduct_whenProductExists() {

        when(repo.existsById(1L))
                .thenReturn(true);


        service.delete(1L);


        verify(repo).existsById(1L);

        verify(repo).deleteById(1L);
    }


    // =========================================================
    // 8. DELETE PRODUCT - NOT FOUND
    // =========================================================

    @Test
    void delete_shouldThrowException_whenProductNotFound() {

        when(repo.existsById(1L))
                .thenReturn(false);


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(1L)
        );


        verify(repo).existsById(1L);

        verify(repo, never())
                .deleteById(anyLong());
    }


    // =========================================================
    // 9. ADD ITEM - SUCCESS
    // =========================================================

    @Test
    void addItem_shouldReturnItemResponse() {

        ItemRequest request =
                new ItemRequest(5);


        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 1L);


        Item item = new Item();

        ReflectionTestUtils.setField(item, "id", 10L);

        item.setProduct(product);
        item.setQuantity(5);


        when(repo.findById(1L))
                .thenReturn(Optional.of(product));

        when(itemRepo.save(any(Item.class)))
                .thenReturn(item);


        ItemResponse result =
                service.addItem(1L, request);


        assertNotNull(result);

        assertEquals(
                10L,
                result.id()
        );

        assertEquals(
                1L,
                result.productId()
        );

        assertEquals(
                5,
                result.quantity()
        );


        verify(repo).findById(1L);

        verify(itemRepo)
                .save(any(Item.class));
    }


    // =========================================================
    // 10. ADD ITEM - PRODUCT NOT FOUND
    // =========================================================

    @Test
    void addItem_shouldThrowException_whenProductNotFound() {

        ItemRequest request =
                new ItemRequest(5);


        when(repo.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.addItem(1L, request)
        );


        verify(repo).findById(1L);

        verify(itemRepo, never())
                .save(any(Item.class));
    }


    // =========================================================
    // 11. GET ITEMS - SUCCESS
    // =========================================================

    @Test
    void getItems_shouldReturnItems() {

        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 1L);


        Item item1 = new Item();

        ReflectionTestUtils.setField(item1, "id", 10L);

        item1.setProduct(product);
        item1.setQuantity(5);


        Item item2 = new Item();

        ReflectionTestUtils.setField(item2, "id", 20L);

        item2.setProduct(product);
        item2.setQuantity(10);


        ReflectionTestUtils.setField(
                product,
                "items",
                List.of(item1, item2)
        );


        when(repo.findById(1L))
                .thenReturn(Optional.of(product));


        List<ItemResponse> result =
                service.getItems(1L);


        assertNotNull(result);

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                10L,
                result.get(0).id()
        );

        assertEquals(
                5,
                result.get(0).quantity()
        );

        assertEquals(
                20L,
                result.get(1).id()
        );

        assertEquals(
                10,
                result.get(1).quantity()
        );


        verify(repo).findById(1L);
    }


    // =========================================================
    // 12. GET ITEMS - PRODUCT NOT FOUND
    // =========================================================

    @Test
    void getItems_shouldThrowException_whenProductNotFound() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getItems(1L)
        );


        verify(repo).findById(1L);
    }


    // =========================================================
    // 13. GET ITEM - SUCCESS
    // =========================================================

    @Test
    void getItem_shouldReturnItem_whenItemBelongsToProduct() {

        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 1L);


        Item item = new Item();

        ReflectionTestUtils.setField(item, "id", 10L);

        item.setProduct(product);
        item.setQuantity(5);


        when(itemRepo.findById(10L))
                .thenReturn(Optional.of(item));


        ItemResponse result =
                service.getItem(1L, 10L);


        assertNotNull(result);

        assertEquals(
                10L,
                result.id()
        );

        assertEquals(
                1L,
                result.productId()
        );

        assertEquals(
                5,
                result.quantity()
        );


        verify(itemRepo).findById(10L);
    }


    // =========================================================
    // 14. GET ITEM - ITEM NOT FOUND
    // =========================================================

    @Test
    void getItem_shouldThrowException_whenItemNotFound() {

        when(itemRepo.findById(10L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getItem(1L, 10L)
        );


        verify(itemRepo).findById(10L);
    }


    // =========================================================
    // 15. GET ITEM - WRONG PRODUCT
    // =========================================================

    @Test
    void getItem_shouldThrowException_whenItemDoesNotBelongToProduct() {

        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 2L);


        Item item = new Item();

        ReflectionTestUtils.setField(item, "id", 10L);

        item.setProduct(product);
        item.setQuantity(5);


        when(itemRepo.findById(10L))
                .thenReturn(Optional.of(item));


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getItem(1L, 10L)
        );


        verify(itemRepo).findById(10L);
    }


    // =========================================================
    // 16. UPDATE ITEM - SUCCESS
    // =========================================================

    @Test
    void updateItem_shouldReturnUpdatedItem() {

        ItemRequest request =
                new ItemRequest(20);


        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 1L);


        Item item = new Item();

        ReflectionTestUtils.setField(item, "id", 10L);

        item.setProduct(product);
        item.setQuantity(5);


        when(itemRepo.findById(10L))
                .thenReturn(Optional.of(item));

        when(itemRepo.save(any(Item.class)))
                .thenReturn(item);


        ItemResponse result =
                service.updateItem(1L, 10L, request);


        assertNotNull(result);

        assertEquals(
                10L,
                result.id()
        );

        assertEquals(
                1L,
                result.productId()
        );

        assertEquals(
                20,
                result.quantity()
        );


        verify(itemRepo).findById(10L);

        verify(itemRepo).save(item);
    }


    // =========================================================
    // 17. UPDATE ITEM - ITEM NOT FOUND
    // =========================================================

    @Test
    void updateItem_shouldThrowException_whenItemNotFound() {

        ItemRequest request =
                new ItemRequest(20);


        when(itemRepo.findById(10L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateItem(1L, 10L, request)
        );


        verify(itemRepo).findById(10L);

        verify(itemRepo, never())
                .save(any(Item.class));
    }


    // =========================================================
    // 18. UPDATE ITEM - WRONG PRODUCT
    // =========================================================

    @Test
    void updateItem_shouldThrowException_whenItemDoesNotBelongToProduct() {

        ItemRequest request =
                new ItemRequest(20);


        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 2L);


        Item item = new Item();

        ReflectionTestUtils.setField(item, "id", 10L);

        item.setProduct(product);
        item.setQuantity(5);


        when(itemRepo.findById(10L))
                .thenReturn(Optional.of(item));


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateItem(1L, 10L, request)
        );


        verify(itemRepo).findById(10L);

        verify(itemRepo, never())
                .save(any(Item.class));
    }


    // =========================================================
    // 19. DELETE ITEM - SUCCESS
    // =========================================================

    @Test
    void deleteItem_shouldDeleteItem() {

        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 1L);


        Item item = new Item();

        ReflectionTestUtils.setField(item, "id", 10L);

        item.setProduct(product);
        item.setQuantity(5);


        when(itemRepo.findById(10L))
                .thenReturn(Optional.of(item));


        service.deleteItem(1L, 10L);


        verify(itemRepo).findById(10L);

        verify(itemRepo).delete(item);
    }


    // =========================================================
    // 20. DELETE ITEM - ITEM NOT FOUND
    // =========================================================

    @Test
    void deleteItem_shouldThrowException_whenItemNotFound() {

        when(itemRepo.findById(10L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteItem(1L, 10L)
        );


        verify(itemRepo).findById(10L);

        verify(itemRepo, never())
                .delete(any(Item.class));
    }


    // =========================================================
    // 21. DELETE ITEM - WRONG PRODUCT
    // =========================================================

    @Test
    void deleteItem_shouldThrowException_whenItemDoesNotBelongToProduct() {

        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", 2L);


        Item item = new Item();

        ReflectionTestUtils.setField(item, "id", 10L);

        item.setProduct(product);
        item.setQuantity(5);


        when(itemRepo.findById(10L))
                .thenReturn(Optional.of(item));


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteItem(1L, 10L)
        );


        verify(itemRepo).findById(10L);

        verify(itemRepo, never())
                .delete(any(Item.class));
    }
}