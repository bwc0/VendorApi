package com.gateway.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.Resource;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Vendor {

    private Long id;
    private String name;
    private List<Resource<Product>> products = new ArrayList<>();

    public Vendor() {
    }

    public Vendor(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Resource<Product>> getProducts() {
        return products;
    }

    public void setProducts(List<Resource<Product>> products) {
        this.products = products;
    }
}
