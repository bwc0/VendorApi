package com.gateway.assembler;

import com.gateway.payload.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Resource;

import static org.junit.jupiter.api.Assertions.*;

class VendorResourceAssemblerTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test_Vendor";
    private VendorResourceAssembler assembler = new VendorResourceAssembler();

    @Test
    @DisplayName("Vendor to Vendor Resource test should return Resource<Vendor>")
    void toResourceTest() {
        Vendor vendor = new Vendor(ID, NAME);

        Resource<Vendor> vendorResource = assembler.toResource(vendor);

        System.out.println(vendorResource.getLinks());
        assertNotNull(vendorResource);
        assertAll(
                () -> assertEquals(vendor.getId(), vendorResource.getContent().getId()),
                () -> assertEquals(vendor.getName(), vendorResource.getContent().getName()),
                () -> assertEquals(3, vendorResource.getLinks().size()),
                () -> assertEquals("/vendors/1", vendorResource.getLinks().get(0).getHref()),
                () -> assertEquals("self", vendorResource.getLinks().get(0).getRel()),
                () -> assertEquals("/vendors/1/products", vendorResource.getLinks().get(1).getHref()),
                () -> assertEquals("products", vendorResource.getLinks().get(1).getRel()),
                () -> assertEquals("/vendors", vendorResource.getLinks().get(2).getHref()),
                () -> assertEquals("vendors", vendorResource.getLinks().get(2).getRel())
        );
    }
}