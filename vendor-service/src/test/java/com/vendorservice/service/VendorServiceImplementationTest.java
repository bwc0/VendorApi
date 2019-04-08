package com.vendorservice.service;

import com.vendorservice.exception.ResourceNotFoundException;
import com.vendorservice.model.Vendor;
import com.vendorservice.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class VendorServiceImplementationTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test_Vendor";
    private Vendor vendor;
    private VendorService service;

    @Mock
    private VendorRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new VendorServiceImplementation(repository);

        vendor = new Vendor(NAME);
        vendor.setId(ID);
    }

    @Test
    @DisplayName("Find vendor by id test")
    void getVendorByIdTest() {
        given(repository.findById(anyLong())).willReturn(Optional.of(vendor));

        Vendor foundVendor = service.getVendorById(1L);

        then(repository).should().findById(anyLong());
        then(repository).should(never()).findAll();
        assertNotNull(foundVendor);
        assertEquals(vendor.getId(), foundVendor.getId());
        assertEquals(vendor.getName(), foundVendor.getName());
    }

    @Test
    @DisplayName("Vendor does not exist test")
    void vendorIdNotFoundTest() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        Throwable e = assertThrows(ResourceNotFoundException.class, () -> service.getVendorById(1L));

        assertEquals("User not found with id: '1'", e.getMessage());
    }

    @Test
    @DisplayName("Update vendor test")
    void updateVendorTest() {
        Vendor updateData = new Vendor("Updated Test Vendor");
        updateData.setId(ID);

        given(repository.save(any())).willReturn(updateData);

        Vendor updatedUser = service.updateVendor(1L, vendor);

        then(repository).should().save(any());
        assertNotNull(updatedUser);
        assertEquals(updateData.getId(), updatedUser.getId());
        assertEquals(updateData.getName(), updatedUser.getName());
    }
}