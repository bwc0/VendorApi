package com.vendorservice.service;

import com.vendorservice.model.Vendor;

public interface VendorService {
    Vendor getVendorById(Long id);
    Vendor updateVendor(Long id, Vendor vendor);
}
