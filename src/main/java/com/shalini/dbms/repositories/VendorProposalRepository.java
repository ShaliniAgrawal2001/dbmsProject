package com.shalini.dbms.repositories;

import com.shalini.dbms.models.VendorProposal;

import java.util.List;

public interface VendorProposalRepository {
    public void add(VendorProposal vendorProposal);
    public List<VendorProposal> findByVendorId(int id);
    public List<VendorProposal> findAll();
}
