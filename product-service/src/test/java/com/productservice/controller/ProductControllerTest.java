package com.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productservice.controller.ExceptionHandler.RestExceptionHandler;
import com.productservice.exceptions.ResourceNotFoundException;
import com.productservice.model.Product;
import com.productservice.repository.ProductRepository;
import com.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

//import static com.productservice.controller.ProductController.PRODUCT_URL;
import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "TEST_VENDOR";
    private static final BigDecimal PRICE = new BigDecimal(10.50);
    private static final String IMAGE = "testImage";
    private static final Long VENDOR_ID = 1L;
    private Product product;
    private MockMvc mockMvc;

    @Mock
    ProductRepository repository;

    @Mock
    ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ProductController controller = new ProductController(repository, service);

        product = new Product(NAME, PRICE, IMAGE, VENDOR_ID);
        product.setId(ID);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET / endpoint should return 200")
    void getAllProducts_ShouldReturn_200Test() throws Exception {

        mockMvc.perform(get( "/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(repository).findAll();
    }

    @Test
    @DisplayName("GET / endpoint should list all products test")
    void getAllProducts_ShouldReturn_ListTest() throws Exception {

        List<Product> data = asList(product, new Product());

        given(repository.findAll()).willReturn(data);

        mockMvc.perform(get("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET /{id} endpoint should return product by id test")
    void getProductById_ShouldReturn200Test() throws Exception {

        mockMvc.perform(get( "/1"))
                .andExpect(status().isOk());

        verify(service).getProductById(anyLong());
    }

    @Test
    @DisplayName("GET /{id} endpoint should list product by id test")
    void getProductById_ShouldReturnVendorTest() throws Exception {

        given(service.getProductById(anyLong())).willReturn(product);

        mockMvc.perform(get( "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())))
                .andExpect(jsonPath("$.price", equalTo(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.image", equalTo(product.getImage())))
                .andExpect(jsonPath("$.vendorId", equalTo(product.getVendorId().intValue())));
    }

    @Test
    @DisplayName("POST / endpoint should return 201 test")
    void createrProduct_ShouldReturn201Test() throws Exception {

        given(repository.save(any())).willReturn(product);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isCreated());

        verify(repository).save(any());
    }

    @Test
    @DisplayName("POST / endpoint should return product test")
    void createProduct_ShouldReturnVendorTest() throws Exception {

        given(repository.save(any())).willReturn(product);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())))
                .andExpect(jsonPath("$.price", equalTo(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.image", equalTo(product.getImage())))
                .andExpect(jsonPath("$.vendorId", equalTo(product.getVendorId().intValue())));
    }

    @Test
    @DisplayName("PUT /{id} endpoint should return 200 test")
    void updateProduct_ShouldReturn200Test() throws Exception {

        given(service.updateProduct(anyLong(), any())).willReturn(product);

        mockMvc.perform(put( "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk());

        verify(service).updateProduct(anyLong(), any());
    }

    @Test
    @DisplayName("PUT /{id} endpoint should return updated product test")
    void updateProduct_ShouldReturnVendorTest() throws Exception {

        given(service.updateProduct(anyLong(), any())).willReturn(product);

        mockMvc.perform(put( "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())))
                .andExpect(jsonPath("$.price", equalTo(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.image", equalTo(product.getImage())))
                .andExpect(jsonPath("$.vendorId", equalTo(product.getVendorId().intValue())));
    }

    @Test
    @DisplayName("DELETE /{id} endpoint should delete product test")
    void deleteProductTest() throws Exception {
        mockMvc.perform(delete( "/1"))
                .andExpect(status().isOk());

        verify(repository).deleteById(anyLong());
    }

    @Test
    @DisplayName("GET /id endpoint test, should return not found test")
    void getTestById_ShouldReturnNotFoundTest() throws Exception {
        given(service.getProductById(anyLong())).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(get( "/1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).getProductById(anyLong());
    }

}