package com.productservice.service;

import com.productservice.exceptions.ResourceNotFoundException;
import com.productservice.model.Product;
import com.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImplementation(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product getProductById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        product.setId(id);
        return repository.save(product);
    }
}
