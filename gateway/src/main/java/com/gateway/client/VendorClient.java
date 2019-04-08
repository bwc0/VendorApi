package com.gateway.client;

import com.gateway.payload.Vendor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("vendor-service")
public interface VendorClient {

    @GetMapping("/")
    List<Vendor> getVendorsPath();

    @GetMapping("/{id}")
    Vendor getVendorByIdPath(@PathVariable("id") Long id);

    @PostMapping("/")
    Vendor getCreateVendorPath(@RequestBody Vendor vendor);

    @PutMapping("/{id}")
    Vendor getUpdateVendorByIdPath(@PathVariable("id") Long id, @RequestBody Vendor vendor);

    @DeleteMapping("/{id}")
    void getDeleteVendorByIdPath(@PathVariable("id") Long id);

    @GetMapping("/{id}")
    Vendor getVendorWithProductsById(@PathVariable("id") Long id);
}