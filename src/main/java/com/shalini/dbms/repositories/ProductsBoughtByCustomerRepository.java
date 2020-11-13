package com.shalini.dbms.repositories;

import com.shalini.dbms.models.ProductsBoughtByCustomer;
import com.shalini.dbms.models.ProductsInCart;

import java.util.List;

public interface ProductsBoughtByCustomerRepository {
    public void addRow(List<ProductsInCart> products, int orderId);

    public List<ProductsBoughtByCustomer> findByOrderId(int id);




}
