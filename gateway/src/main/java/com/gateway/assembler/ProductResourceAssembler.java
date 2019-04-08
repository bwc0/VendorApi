package com.gateway.assembler;

import com.gateway.controller.ProductController;
import com.gateway.controller.VendorController;
import com.gateway.payload.Product;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ProductResourceAssembler implements ResourceAssembler<Product, Resource<Product>> {

    @Override
    public Resource<Product> toResource(Product product) {

        Resource<Product> productResource = new Resource<>(product,
                linkTo(methodOn(ProductController.class).getProductById(product.getId())).withSelfRel());

        if (product.getVendorId() != null) {
            productResource.add(
                    linkTo(methodOn(VendorController.class)
                            .getProductsByVendorId(product.getVendorId())).withRel("vendor"),
                    linkTo(methodOn(VendorController.class).getProductByVendorIdAndProductId(product.getVendorId(),
                            product.getId())).withSelfRel());
        } else {
            productResource.add(
                    linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));
        }

        return productResource;
    }
}
