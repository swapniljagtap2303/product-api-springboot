package com.zestindia.productapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestindia.productapi.dto.DTOs.ItemRequest;
import com.zestindia.productapi.dto.DTOs.ItemResponse;
import com.zestindia.productapi.dto.DTOs.ProductRequest;
import com.zestindia.productapi.dto.DTOs.ProductResponse;
import com.zestindia.productapi.security.JwtAuthFilter;
import com.zestindia.productapi.security.JwtService;
import com.zestindia.productapi.service.ProductService;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService service;

    @MockBean
    JwtService jwtService;

    @MockBean
    JwtAuthFilter jwtAuthFilter;


    // =========================================================
    // 1. CREATE PRODUCT - POST
    // =========================================================

    @Test
    void create_shouldReturn201() throws Exception {

        ProductRequest request =
                new ProductRequest("Laptop", "Swapnil");

        ProductResponse response =
                new ProductResponse(
                        1L,
                        "Laptop",
                        "Swapnil",
                        null,
                        null,
                        null
                );

        when(service.create(any(ProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/api/v1/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.productName").value("Laptop"))
        .andExpect(jsonPath("$.createdBy").value("Swapnil"));
    }


    // =========================================================
    // 2. GET ALL PRODUCTS - GET
    // =========================================================

    @Test
    void all_shouldReturnProducts() throws Exception {

        ProductResponse product1 =
                new ProductResponse(
                        1L,
                        "Laptop",
                        "Swapnil",
                        null,
                        null,
                        null
                );

        ProductResponse product2 =
                new ProductResponse(
                        2L,
                        "Mobile",
                        "Rahul",
                        null,
                        null,
                        null
                );

        PageImpl<ProductResponse> page =
                new PageImpl<>(
                        List.of(product1, product2),
                        PageRequest.of(0, 10),
                        2
                );

        when(service.all(any()))
                .thenReturn(page);

        mockMvc.perform(
                get("/api/v1/products")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].id").value(1))
        .andExpect(jsonPath("$.content[0].productName").value("Laptop"))
        .andExpect(jsonPath("$.content[1].id").value(2))
        .andExpect(jsonPath("$.content[1].productName").value("Mobile"));
    }


    // =========================================================
    // 3. GET PRODUCT BY ID - GET
    // =========================================================

    @Test
    void get_shouldReturnProduct() throws Exception {

        ProductResponse response =
                new ProductResponse(
                        1L,
                        "Laptop",
                        "Swapnil",
                        null,
                        null,
                        null
                );

        when(service.get(1L))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/v1/products/1")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.productName").value("Laptop"))
        .andExpect(jsonPath("$.createdBy").value("Swapnil"));
    }


    // =========================================================
    // 4. UPDATE PRODUCT - PUT
    // =========================================================

    @Test
    void update_shouldReturnUpdatedProduct() throws Exception {

        ProductRequest request =
                new ProductRequest("Gaming Laptop", "Swapnil");

        ProductResponse response =
                new ProductResponse(
                        1L,
                        "Gaming Laptop",
                        "Swapnil",
                        null,
                        null,
                        null
                );

        when(service.update(
                any(Long.class),
                any(ProductRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                put("/api/v1/products/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.productName").value("Gaming Laptop"));
    }


    // =========================================================
    // 5. DELETE PRODUCT - DELETE
    // =========================================================

    @Test
    void delete_shouldReturn204() throws Exception {

        doNothing().when(service).delete(1L);

        mockMvc.perform(
                delete("/api/v1/products/1")
        )
        .andExpect(status().isNoContent());
    }


    // =========================================================
    // 6. ADD ITEM - POST
    // =========================================================

    @Test
    void addItem_shouldReturn201() throws Exception {

        ItemRequest request =
                new ItemRequest(5);

        ItemResponse response =
                new ItemResponse(
                        10L,
                        1L,
                        5
                );

        when(service.addItem(
                any(Long.class),
                any(ItemRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                post("/api/v1/products/1/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.productId").value(1))
        .andExpect(jsonPath("$.quantity").value(5));
    }


    // =========================================================
    // 7. GET ALL ITEMS - GET
    // =========================================================

    @Test
    void getItems_shouldReturnItems() throws Exception {

        ItemResponse item1 =
                new ItemResponse(
                        10L,
                        1L,
                        5
                );

        ItemResponse item2 =
                new ItemResponse(
                        11L,
                        1L,
                        10
                );

        when(service.getItems(1L))
                .thenReturn(List.of(item1, item2));

        mockMvc.perform(
                get("/api/v1/products/1/items")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(10))
        .andExpect(jsonPath("$[0].quantity").value(5))
        .andExpect(jsonPath("$[1].id").value(11))
        .andExpect(jsonPath("$[1].quantity").value(10));
    }


    // =========================================================
    // 8. GET ITEM BY ID - GET
    // =========================================================

    @Test
    void getItem_shouldReturnItem() throws Exception {

        ItemResponse response =
                new ItemResponse(
                        10L,
                        1L,
                        5
                );

        when(service.getItem(1L, 10L))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/v1/products/1/items/10")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.productId").value(1))
        .andExpect(jsonPath("$.quantity").value(5));
    }


    // =========================================================
    // 9. UPDATE ITEM - PUT
    // =========================================================

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {

        ItemRequest request =
                new ItemRequest(20);

        ItemResponse response =
                new ItemResponse(
                        10L,
                        1L,
                        20
                );

        when(service.updateItem(
                any(Long.class),
                any(Long.class),
                any(ItemRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                put("/api/v1/products/1/items/10")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.productId").value(1))
        .andExpect(jsonPath("$.quantity").value(20));
    }


    // =========================================================
    // 10. DELETE ITEM - DELETE
    // =========================================================

    @Test
    void deleteItem_shouldReturn204() throws Exception {

        doNothing().when(service)
                .deleteItem(1L, 10L);

        mockMvc.perform(
                delete("/api/v1/products/1/items/10")
        )
        .andExpect(status().isNoContent());
    }
}