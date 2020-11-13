package com.shalini.dbms.repositories;

import com.shalini.dbms.models.VendorShopAddress;

import java.util.List;

public interface VendorShopAddressRepository {
    public void add(VendorShopAddress vendorShopAddress);
    public VendorShopAddress findByVendorId(int id);
    public void update(VendorShopAddress vendorShopAddress);
}
