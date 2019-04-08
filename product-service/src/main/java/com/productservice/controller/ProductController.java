package com.productservice.controller;

import com.productservice.model.Product;
import com.productservice.repository.ProductRepository;
import com.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import static com.productservice.controller.ProductController.PRODUCT_URL;

@RestController
//@RequestMapping(PRODUCT_URL)
public class ProductController {

    private final ProductRepository repository;
    private final ProductService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

//    public static final String PRODUCT_URL = "/api/v1/products";

    public ProductController(ProductRepository repository, ProductService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        LOGGER.info("Gathering products...");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProductById(@PathVariable Long id) {
        LOGGER.info("Gathering product: " + id);
        return service.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product) {
        LOGGER.info("Creating product ...");
        return repository.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProductById(@PathVariable Long id, @RequestBody Product product) {
        LOGGER.info("Updating product: " + id);
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        LOGGER.info("Deleting product: " + id);
        repository.deleteById(id);
    }
}
