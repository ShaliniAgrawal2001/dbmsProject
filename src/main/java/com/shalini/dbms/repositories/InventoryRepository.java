package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Inventory;
import com.shalini.dbms.models.ProductsInCart;

import java.awt.*;
import java.util.List;

public interface InventoryRepository {
    public List<Inventory> findByProductId(int id);
    public int findQuantity(int id, String size);
    public Inventory findByProductAndSize(int product_id, String size);
    public void updateQty(List<ProductsInCart> productsInCart);
    public void updateQtyOfProduct(Inventory inventory);
    public void add (Inventory inventory);
}
