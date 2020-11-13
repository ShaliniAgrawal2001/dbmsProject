package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Order;

import java.util.List;

public interface OrderRepository {
    public void addRow(Order order);
    public List<Order> notRecievedOrder();
    public void markRecieved(int id);
    public Order getId(String timeOfPurchase);
    public List<Order> findAll(int customerId);
    public Order findById(int id);
}
