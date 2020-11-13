package com.shalini.dbms.models;

public class Order {
    private int id;
    private String timeOfPurchase;
    private int totalAmount;
    private String recievedFromStore;
    private User customer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeOfPurchase() {
        return timeOfPurchase;
    }

    public void setTimeOfPurchase(String timeOfPurchase) {
        this.timeOfPurchase = timeOfPurchase;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRecievedFromStore() {
        return recievedFromStore;
    }

    public void setRecievedFromStore(String recievedFromStore) {
        this.recievedFromStore = recievedFromStore;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }
}
