package com.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.assembler.ProductResourceAssembler;
import com.gateway.assembler.VendorResourceAssembler;
import com.gateway.client.ProductClient;
import com.gateway.client.VendorClient;
import com.gateway.exception.CustomFeignException;
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
@WebMvcTest(VendorController.class)
class VendorControllerTest {

    @MockBean
    private VendorClient vendorClient;

    @MockBean
    private ProductClient productClient;

    @MockBean
    private VendorResourceAssembler vendorAssembler;

    @MockBean
    private ProductResourceAssembler productAssembler;

    @Autowired
    private MockMvc mockMvc;

    private Vendor vendor1;
    private Resource<Vendor> vendorResource;
    private Product product1;
    private Resource<Product> productResource;

    @BeforeEach
    void setUp() {
        vendor1 = new Vendor(1L, "TestVendor1");

        product1 = new Product(1L, "product1", new BigDecimal(10.5), "image", vendor1.getId());

        vendorResource = new Resource<>(vendor1);
        productResource = new Resource<>(product1);
    }

    @Test
    @DisplayName("GET /vendors should return list of vendors with resources and 200 status")
    void getAllVendors_shouldReturnListOfVendors_and200Test() throws Exception {
        given(vendorClient.getVendorsPath()).willReturn(Collections.singletonList(vendor1));
        given(vendorAssembler.toResource(any())).willReturn(vendorResource);

        mockMvc.perform(get("/vendors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.vendorList", hasSize(1)))
                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/vendors")))
                .andDo(print());

        verify(vendorClient).getVendorsPath();
        verify(vendorAssembler).toResource(any());
    }

    @Test
    @DisplayName("GET /vendors/{id} should return vendor by id with resources and 200 status")
    void getVendorById_shouldReturnVendor_and200Test() throws Exception {
        given(vendorAssembler.toResource(any())).willReturn(vendorResource);
        given(vendorClient.getVendorByIdPath(anyLong())).willReturn(vendor1);

        mockMvc.perform(get("/vendors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(vendor1.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(vendor1.getName())))
//                TODO: Links not showing up in test?
//                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/vendors/1")))
//                .andExpect(jsonPath("$._links.products.href",
//                        equalTo("http://localhost/vendors/1/products")))
//                .andExpect(jsonPath("$._links.vendors.href", equalTo("http://localhost/vendors")))
                .andDo(print());

        verify(vendorClient).getVendorByIdPath(anyLong());
        verify(vendorAssembler).toResource(any());
    }

    @Test
    @DisplayName("GET /vendors/{id} should throw FeignClient exception")
    void getVendorById_shouldThrowFeignException() throws Exception {
        given(vendorClient.getVendorByIdPath(anyLong())).willThrow(FeignException.class);

        mockMvc.perform(get("/vendors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("POST /vendors should save new vendor and return vendor and 200 status")
    void createVendor_shouldReturnSavedVendor_andReturn200() throws Exception {
        given(vendorClient.getCreateVendorPath(any())).willReturn(vendor1);
        given(vendorAssembler.toResource(any())).willReturn(vendorResource);

        mockMvc.perform(post("/vendors")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vendor1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(vendor1.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(vendor1.getName())))
//                TODO: Links not showing up in test?
//                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/vendors/1")))
//                .andExpect(jsonPath("$._links.products.href",
//                        equalTo("http://localhost/vendors/1/products")))
//                .andExpect(jsonPath("$._links.vendors.href", equalTo("http://localhost/vendors")))
                .andDo(print());

        verify(vendorClient).getCreateVendorPath(any());
        verify(vendorAssembler).toResource(any());
    }

    @Test
    @DisplayName("Put /vendors/{id} should update vendor and return vendor and 200 status")
    void updateVendor_shouldUpdateVendor_andReturn200Test() throws Exception {
        given(vendorClient.getUpdateVendorByIdPath(anyLong(), any())).willReturn(vendor1);
        given(vendorAssembler.toResource(any())).willReturn(vendorResource);

        mockMvc.perform(put("/vendors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vendor1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(vendor1.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(vendor1.getName())))
//                TODO: Links not showing up in test?
//                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/vendors/1")))
//                .andExpect(jsonPath("$._links.products.href",
//                        equalTo("http://localhost/vendors/1/products")))
//                .andExpect(jsonPath("$._links.vendors.href", equalTo("http://localhost/vendors")))
                .andDo(print());

        verify(vendorClient).getUpdateVendorByIdPath(anyLong(), any());
        verify(vendorAssembler).toResource(any());
    }

    @Test
    @DisplayName("Delete /{id} vendor by id should return 200 test")
    void deleteVendor_shouldDeleteVendor_andReturn200() throws Exception {
        mockMvc.perform(delete("/vendors/1"))
                .andExpect(status().isOk());

        verify(vendorClient).getDeleteVendorByIdPath(anyLong());
    }

    @Test
    @DisplayName("Get /vendors/{id}/products should return vendor products and 200")
    void getProductsByVendorIdTest() throws Exception {
        vendor1.getProducts().add(productResource);
        given(vendorClient.getVendorWithProductsById(anyLong())).willReturn(vendor1);

        mockMvc.perform(get("/vendors/1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productList", hasSize(1)))
                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/products")))
                .andDo(print());

        verify(vendorClient).getVendorWithProductsById(anyLong());
    }

    @Test
    @DisplayName("Get /vendors/{id}/products should throw feign Exception test")
    void getProductsByVendorId_shouldThrowFiegnExceptionTest() throws Exception {
        vendor1.getProducts().add(productResource);
        given(vendorClient.getVendorWithProductsById(anyLong())).willThrow(FeignException.class);

        mockMvc.perform(get("/vendors/1/products")
                .contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(vendorClient).getVendorWithProductsById(anyLong());
    }

    @Test
    @DisplayName("Get /vendor/{id}/products/{id} should return product")
    void getProductByVendorIdAndProductIdTest() throws Exception {
        vendor1.getProducts().add(productResource);
        given(productClient.getProductsPath()).willReturn(Collections.singletonList(product1));
        given(productAssembler.toResource(any())).willReturn(productResource);

        mockMvc.perform(get("/vendors/1/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(product1.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product1.getName())))
                .andExpect(jsonPath("$.price", equalTo(product1.getPrice().doubleValue())))
                .andExpect(jsonPath("$.imageUrl", equalTo(product1.getImageUrl())))
                .andExpect(jsonPath("$.vendorId", equalTo(product1.getVendorId().intValue())))
                .andDo(print());

        verify(productClient).getProductsPath();
        verify(productAssembler).toResource(any());
    }

    @Test
    @DisplayName("Get /vendor/{id}/products/{id} should return product")
    void getProductByVendorIdAndProductId_shouldThrowCustomFeignException_Test() throws Exception {
        vendor1.getProducts().add(productResource);
        given(productClient.getProductsPath()).willThrow(CustomFeignException.class);
        given(productAssembler.toResource(any())).willReturn(productResource);

        mockMvc.perform(get("/vendors/1/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}