package com.vendorservice.controller;

import com.vendorservice.model.Vendor;
import com.vendorservice.repository.VendorRepository;
import com.vendorservice.service.VendorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import static com.vendorservice.controller.VendorController.VENDOR_URL;

@RestController
public class VendorController {

    private final VendorRepository repository;
    private final VendorService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(VendorController.class);

    public VendorController(VendorRepository repository, VendorService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Vendor> getAllVendors() {
        LOGGER.info("Gathering vendors...");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Vendor getVendorById(@PathVariable Long id) {
        LOGGER.info("Gathering vendor: " + id);
        return service.getVendorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vendor createVendor(@RequestBody Vendor vendor) {
        LOGGER.info("Creating Vendor ...");
        return repository.save(vendor);
    }

    @PutMapping("/{id}")
    public Vendor updateVendorById(@PathVariable Long id, @RequestBody Vendor vendor) {
        LOGGER.info("Updating vendor: " + id);
        return service.updateVendor(id, vendor);
    }

    @DeleteMapping("/{id}")
    public void deleteVendorById(@PathVariable Long id) {
        LOGGER.info("Deleting vendor: " + id);
        repository.deleteById(id);
    }
}
