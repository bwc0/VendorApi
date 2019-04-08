package com.gateway.controller;

import com.gateway.assembler.ProductResourceAssembler;
import com.gateway.assembler.VendorResourceAssembler;
import com.gateway.client.ProductClient;
import com.gateway.client.VendorClient;
import com.gateway.exception.CustomFeignException;
import com.gateway.payload.Product;
import com.gateway.payload.Vendor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(description = "Vendor Operation")
@RestController
@RequestMapping("/vendors")
public class VendorController {

    private final VendorClient vendorClient;
    private final ProductClient productClient;
    private final VendorResourceAssembler vendorResourceAssembler;
    private final ProductResourceAssembler productResourceAssembler;
    private static final Logger LOGGER = LoggerFactory.getLogger(VendorController.class);

    public VendorController(VendorClient vendorClient, ProductClient productClient,
                            VendorResourceAssembler vendorResourceAssembler,
                            ProductResourceAssembler productResourceAssembler) {
        this.vendorClient = vendorClient;
        this.productClient = productClient;
        this.vendorResourceAssembler = vendorResourceAssembler;
        this.productResourceAssembler = productResourceAssembler;
    }

    @ApiOperation(value = "Get all Vendors", notes = "Will return a list of vendors")
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Resources<Resource<Vendor>> getAllVendors() {

        LOGGER.info("Getting vendors from vendors-service");

        List<Resource<Vendor>> vendors = vendorClient
                .getVendorsPath()
                .stream()
                .map(vendorResourceAssembler::toResource)
                .collect(Collectors.toList());

        LOGGER.info("Mapping vendors to VendorResource and adding to List");

        return new Resources<>(vendors,
                linkTo(methodOn(VendorController.class).getAllVendors()).withSelfRel());
    }

    @ApiOperation(value = "Get Vendor by ID", notes = "Will return a specific vendor")
    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Vendor> getVendorById(@PathVariable("id") Long id) {
        LOGGER.info("Getting vendor by id: " + id);

        return vendorResourceAssembler.toResource(vendorClient.getVendorByIdPath(id));
    }

    @ApiOperation(value = "Create a Vendor", notes = "Will create a vendor")
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Vendor> createVendor(@RequestBody Vendor vendor) {

        LOGGER.info("Creating vendor");

        return vendorResourceAssembler.toResource(vendorClient.getCreateVendorPath(vendor));
    }

    @ApiOperation(value = "Update a Vendor", notes = "Will update a vendor")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Vendor> updateVendor(@PathVariable("id") Long id, @RequestBody Vendor vendor) {

        LOGGER.info("Updating vendor by id: " + id);

        return vendorResourceAssembler.toResource(vendorClient.getUpdateVendorByIdPath(id, vendor));
    }

    @ApiOperation(value = "Delete a Vendor", notes = "Will delete a vendor")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVendor(@PathVariable("id") Long id) {

        LOGGER.info("Deleting vendor by id: " + id);

        vendorClient.getDeleteVendorByIdPath(id);
    }

    @ApiOperation(value = "Get Vendor product list", notes = "Will return vendors list of products")
    @GetMapping("/{id}/products")
    @ResponseStatus(HttpStatus.OK)
    public Resources<Resource<Product>> getProductsByVendorId(@PathVariable("id") Long id) {
        Vendor vendor = vendorClient.getVendorWithProductsById(id);

        LOGGER.info("Searching for vendors products");
        vendor.getProducts().addAll(getProductResourceList(id));

        LOGGER.info("Listing products");
        List<Resource<Product>> products = vendor.getProducts();

        return new Resources<>(products,
                linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
    }

    @ApiOperation(value = "Get Product by Id and Vendor Id", notes = "Will return a specific product by Id and VendorId")
    @GetMapping("/{vendor_id}/products/{product_id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Product> getProductByVendorIdAndProductId(@PathVariable("vendor_id") Long vendor_id,
                                                  @PathVariable("product_id") Long product_id) {

        LOGGER.info("List vendor's products");

        return  getProductList(vendor_id)
                .stream()
                .filter(product -> product.getId().equals(product_id))
                .map(productResourceAssembler::toResource)
                .findFirst()
                .orElseThrow(() -> new CustomFeignException("Product not found with id: " + product_id));
    }

    private List<Product> getProductList(Long id) {
        return productClient.getProductsPath()
                .stream()
                .filter(product -> {
                    if(product.getVendorId().equals(id)) {
                        return true;
                    }
                    throw new CustomFeignException("Product doesn't belong to vendor id:" + id);
                })
                .collect(Collectors.toList());
    }

    private List<Resource<Product>> getProductResourceList(Long id) {
        return productClient.getProductsPath()
                .stream()
                .filter(product -> product.getVendorId().equals(id))
                .map(productResourceAssembler::toResource)
                .collect(Collectors.toList());
    }
}
