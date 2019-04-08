package com.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.assembler.ProductResourceAssembler;
import com.gateway.client.ProductClient;
import com.gateway.payload.Product;
import com.gateway.payload.Vendor;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockBean
    private ProductClient client;

    @MockBean
    private ProductResourceAssembler assembler;

    @Autowired
    private MockMvc mockMvc;

    private Product product;
    private Vendor vendor;
    private Resource<Product> productResource;

    @BeforeEach
    void setUp() {
        vendor = new Vendor();
        vendor.setId(1L);

        product = new Product(1L, "product1", new BigDecimal(10.5), "image", vendor.getId());

        productResource = new Resource<>(product);
    }

    @Test
    @DisplayName("Get /products should return list of products with resources and 200 status")
    void getAllProducts_shouldReturnProductListWithResources_and200() throws Exception{
        given(client.getProductsPath()).willReturn(Collections.singletonList(product));
        given(assembler.toResource(any())).willReturn(productResource);

        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productList", hasSize(1)))
                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/products")))
                .andDo(print());

        verify(client).getProductsPath();
        verify(assembler).toResource(any());
    }

    @Test
    @DisplayName("Get /products/{id} should return product and 200 status")
    void getProductById_shouldReturnProduct_and200Status() throws Exception {
        given(assembler.toResource(any())).willReturn(productResource);
        given(client.getProductByIdPath(anyLong())).willReturn(product);

        mockMvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())))
                .andExpect(jsonPath("$.price", equalTo(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.imageUrl", equalTo(product.getImageUrl())))
                .andExpect(jsonPath("$.vendorId", equalTo(product.getVendorId().intValue())))
                .andDo(print());

        verify(client).getProductByIdPath(anyLong());
        verify(assembler).toResource(any());
    }

    @Test
    @DisplayName("GET /products/{id} should throw FeignClient exception")
    void getProductById_shouldThrowFeignException() throws Exception {
        given(client.getProductByIdPath(anyLong())).willThrow(FeignException.class);

        mockMvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("POST /products should save new product and return product and 200 status")
    void createProduct_shouldReturnSavedProduct_andReturn200() throws Exception {
        given(client.getCreateProductPath(any())).willReturn(product);
        given(assembler.toResource(any())).willReturn(productResource);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())))
                .andExpect(jsonPath("$.price", equalTo(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.imageUrl", equalTo(product.getImageUrl())))
                .andExpect(jsonPath("$.vendorId", equalTo(product.getVendorId().intValue())))
//                TODO: Links not showing up in test?
                .andDo(print());

        verify(client).getCreateProductPath(any());
        verify(assembler).toResource(any());
    }

    @Test
    @DisplayName("Put /products/{id} should update product and return product and 200 status")
    void updateProduct_shouldUpdateProduct_andReturn200Test() throws Exception {
        given(client.getPutUpdateProductByIdPath(anyLong(), any())).willReturn(product);
        given(assembler.toResource(any())).willReturn(productResource);

        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())))
                .andExpect(jsonPath("$.price", equalTo(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.imageUrl", equalTo(product.getImageUrl())))
                .andExpect(jsonPath("$.vendorId", equalTo(product.getVendorId().intValue())))
//                TODO: Links not showing up in test?
                .andDo(print());

        verify(client).getPutUpdateProductByIdPath(anyLong(), any());
        verify(assembler).toResource(any());
    }

    @Test
    @DisplayName("Delete /{id} product by id should return 200 test")
    void deleteProduct_shouldDeleteProduct_andReturn200() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());

        verify(client).getDeleteProductByIdPath(anyLong());
    }
}