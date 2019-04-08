package com.gateway.assembler;

import com.gateway.payload.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Resource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductResourceAssemblerTest {

    private static final Long ID = 1L;
    private static final String NAME = "test product";
    private static final BigDecimal PRICE = new BigDecimal(10.50);
    private static final String IMAGEURL = "test image";
    private static final Long VENDORID = 1L;
    private ProductResourceAssembler assembler = new ProductResourceAssembler();

    private Product product;

    @Test
    @DisplayName("Product to Product Resource with vendorId not null should return Resource<Product>")
    void toResource_withVendorIdNotNull_Test() {
        product = new Product(ID, NAME, PRICE, IMAGEURL, VENDORID);

        Resource<Product> productResource = assembler.toResource(product);

        System.out.println(productResource.getLinks());
        assertNotNull(productResource);
        assertAll(
                () -> assertEquals(product.getId(), productResource.getContent().getId()),
                () -> assertEquals(product.getName(), productResource.getContent().getName()),
                () -> assertEquals(product.getImageUrl(), productResource.getContent().getImageUrl()),
                () -> assertEquals(product.getPrice(), productResource.getContent().getPrice()),
                () -> assertEquals(product.getVendorId(), productResource.getContent().getVendorId()),
                () -> assertEquals("/products/1", productResource.getLinks().get(0).getHref()),
                () -> assertEquals("self", productResource.getLinks().get(0).getRel()),
                () -> assertEquals("/vendors/1/products", productResource.getLinks().get(1).getHref()),
                () -> assertEquals("vendor", productResource.getLinks().get(1).getRel()),
                () -> assertEquals("/vendors/1/products/1", productResource.getLinks().get(2).getHref()),
                () -> assertEquals("self", productResource.getLinks().get(2).getRel())
        );
    }

    @Test
    @DisplayName("Product to Product Resource with vendorId null should return Resource<Product>")
    void toResource_withVendorIdNull_Test() {
        product = new Product();
        product.setId(ID);
        product.setName(NAME);
        product.setPrice(PRICE);
        product.setImageUrl(IMAGEURL);

        Resource<Product> productResource = assembler.toResource(product);

        System.out.println(productResource.getLinks());
        assertNotNull(productResource);
        assertAll(
                () -> assertEquals(product.getId(), productResource.getContent().getId()),
                () -> assertEquals(product.getName(), productResource.getContent().getName()),
                () -> assertEquals(product.getImageUrl(), productResource.getContent().getImageUrl()),
                () -> assertEquals(product.getPrice(), productResource.getContent().getPrice()),
                () -> assertEquals(product.getVendorId(), productResource.getContent().getVendorId()),
                () -> assertEquals("/products/1", productResource.getLinks().get(0).getHref()),
                () -> assertEquals("self", productResource.getLinks().get(0).getRel()),
                () -> assertEquals("/products", productResource.getLinks().get(1).getHref()),
                () -> assertEquals("products", productResource.getLinks().get(1).getRel())
        );
    }
}