package com.shalini.dbms.models;

public class Inventory {
    private int id;
    private Product product;
    private int inhouseqty;
    private String size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getInhouseqty() {
        return inhouseqty;
    }

    public void setInhouseqty(int inhouseqty) {
        this.inhouseqty = inhouseqty;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
