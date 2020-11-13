package com.shalini.dbms.repositories;

import com.shalini.dbms.models.ProductsInCart;

import java.util.List;

public interface ProductsInCartRepository {
    public List<ProductsInCart> findProductsByCartId(int id);
    public void addToCart(ProductsInCart productsInCart);
    public int getTotalPrice(int cartId);
    public void removeFromCart(int id);
    public ProductsInCart findBySizeCartIdProductId(String size, int cartId, int productId);
    public void updateQty(int qty, int id);
    public void emptyCart(int id);
    public ProductsInCart findById(int id);
}
