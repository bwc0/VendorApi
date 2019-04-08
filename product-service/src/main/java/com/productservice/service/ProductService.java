package com.productservice.service;

import com.productservice.model.Product;

public interface ProductService {
    Product getProductById(Long id);
    Product updateProduct(Long id, Product product);
}
