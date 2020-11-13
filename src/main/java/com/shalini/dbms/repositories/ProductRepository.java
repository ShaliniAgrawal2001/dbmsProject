package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Product;

import java.util.List;

public interface ProductRepository {
    public void addProduct(Product product);
    public List<Product> findAll();
    public List<Product> findDiscounted();
    public Product findById(int id);
    public List<Product> findByCategoryId(int id);
    public void updateProductInfo(Product product);
}
