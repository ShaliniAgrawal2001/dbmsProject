package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Cart;
import com.shalini.dbms.models.User;

public interface CartRepository {
    public Cart findByCustomerId(int id);
    public Cart findById(int id);
    public void addCart(User user);
    public void updateTotalPrice(int id, int price);

}
