package com.gateway.controller;

import com.gateway.assembler.ProductResourceAssembler;
import com.gateway.client.ProductClient;
import com.gateway.payload.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(description = "Product Operation")
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductClient client;
    private final ProductResourceAssembler assembler;

    public ProductController(ProductClient client, ProductResourceAssembler assembler) {
        this.client = client;
        this.assembler = assembler;
    }

    @ApiOperation(value = "Get all Products", notes = "Will return a list of products")
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Resources<Resource<Product>> getAllProducts() {
        List<Resource<Product>> products = client
                .getProductsPath()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(products,
                linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
    }

    @ApiOperation(value = "Get Product by ID", notes = "Will return a specific product")
    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Product> getProductById(@PathVariable("id") Long id) {
        return assembler.toResource(client.getProductByIdPath(id));
    }

    @ApiOperation(value = "Create a Product", notes = "Will create a product")
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Product> createProduct(@RequestBody Product product) {
        return assembler.toResource(client.getCreateProductPath(product));
    }

    @ApiOperation(value = "Update a Product", notes = "Will update a product")
    @PutMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Product> putUpdateVendor(@PathVariable("id") Long id, @RequestBody Product product) {
        return assembler.toResource(client.getPutUpdateProductByIdPath(id, product));
    }

    @ApiOperation(value = "Delete a Product", notes = "Will delete a product")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVendor(@PathVariable("id") Long id) {
        client.getDeleteProductByIdPath(id);
    }
}
