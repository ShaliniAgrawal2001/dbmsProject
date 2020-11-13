package com.shalini.dbms.events;

import com.shalini.dbms.models.ProductsBoughtByCustomer;
import com.shalini.dbms.models.ProductsInCart;
import org.springframework.context.ApplicationEvent;


import java.util.List;

public class OnPaymentSuccessEvent extends ApplicationEvent {

    private List<ProductsInCart> products;

    public OnPaymentSuccessEvent(List<ProductsInCart> products) {
        super(products);
        this.products = products;
    }

    public List<ProductsInCart> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsInCart> products) {
        this.products = products;
    }
}
