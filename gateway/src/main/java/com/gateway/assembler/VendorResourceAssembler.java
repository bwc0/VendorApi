package com.gateway.assembler;

import com.gateway.controller.VendorController;
import com.gateway.payload.Vendor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class VendorResourceAssembler implements ResourceAssembler<Vendor, Resource<Vendor>> {

    @Override
    public Resource<Vendor> toResource(Vendor vendor) {
        return new Resource<>(vendor,
                linkTo(methodOn(VendorController.class).getVendorById(vendor.getId())).withSelfRel(),
                linkTo(methodOn(VendorController.class).getProductsByVendorId(vendor.getId())).withRel("products"),
                linkTo(methodOn(VendorController.class).getAllVendors()).withRel("vendors"));
    }
}
