package com.vendorservice.service;

import com.vendorservice.exception.ResourceNotFoundException;
import com.vendorservice.model.Vendor;
import com.vendorservice.repository.VendorRepository;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceImplementation implements VendorService {

    private VendorRepository repository;

    public VendorServiceImplementation(VendorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Vendor getVendorById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public Vendor updateVendor(Long id, Vendor vendor) {
        vendor.setId(id);
        return repository.save(vendor);
    }
}
