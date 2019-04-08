package com.gateway.client;

import com.gateway.payload.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("product-service")
public interface ProductClient {

    @GetMapping("/")
    List<Product> getProductsPath();

    @GetMapping("/{id}")
    Product getProductByIdPath(@PathVariable("id") Long id);

    @PostMapping("/")
    Product getCreateProductPath(@RequestBody Product product);

    @PutMapping("/{id}")
    Product getPutUpdateProductByIdPath(@PathVariable("id") Long id, @RequestBody Product product);

    @DeleteMapping("/{id}")
    void getDeleteProductByIdPath(@PathVariable("id") Long id);
}
