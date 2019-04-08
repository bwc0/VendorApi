package com.productservice.service;

import com.productservice.exceptions.ResourceNotFoundException;
import com.productservice.model.Product;
import com.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class ProductServiceImplementationTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test_Product";
    private static final BigDecimal PRICE = new BigDecimal(10.00);
    private static final String IMAGE = "testImage";
    private static final Long VENDOR_ID = 1L;
    private Product product;
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new ProductServiceImplementation(repository);

        product = new Product(NAME, PRICE, IMAGE, VENDOR_ID);
        product.setId(ID);
    }

    @Test
    @DisplayName("Find product by id test")
    void getProductByIdTest() {
        given(repository.findById(anyLong())).willReturn(Optional.of(product));

        Product foundProduct = service.getProductById(1L);

        then(repository).should().findById(anyLong());
        then(repository).should(never()).findAll();
        assertNotNull(foundProduct);
        assertAll(
                () -> assertEquals(product.getId(), foundProduct.getId()),
                () -> assertEquals(product.getName(), foundProduct.getName()),
                () -> assertEquals(product.getPrice(), foundProduct.getPrice()),
                () -> assertEquals(product.getImage(), foundProduct.getImage()),
                () -> assertEquals(product.getVendorId(), foundProduct.getVendorId())
        );
    }

    @Test
    @DisplayName("Product does not exist test")
    void vendorIdNotFoundTest() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        Throwable e = assertThrows(ResourceNotFoundException.class, () -> service.getProductById(1L));

        assertEquals("Product not found with id: '1'", e.getMessage());
    }

    @Test
    @DisplayName("Update product test")
    void updateProductTest() {
        Product updateData = new Product();
        updateData.setId(ID);

        given(repository.save(any())).willReturn(updateData);

        Product updatedProduct = service.updateProduct(1L, product);

        then(repository).should().save(any());
        assertNotNull(updatedProduct);
        assertAll(
                () -> assertEquals(updateData.getId(), updatedProduct.getId()),
                () -> assertEquals(updateData.getName(), updatedProduct.getName()),
                () -> assertEquals(updateData.getPrice(), updatedProduct.getPrice()),
                () -> assertEquals(updateData.getImage(), updatedProduct.getImage()),
                () -> assertEquals(updateData.getVendorId(), updatedProduct.getVendorId())
        );
    }
}