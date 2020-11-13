package com.shalini.dbms.repositories;

import com.shalini.dbms.models.VendorProposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VendorProposalRepositoryImpl implements VendorProposalRepository {
    JdbcTemplate jdbcTemplate;
    UserRepository userRepository;

    @Autowired
    public VendorProposalRepositoryImpl(JdbcTemplate jdbcTemplate,UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    private RowMapper<VendorProposal> vendorProposalRowMapper= new RowMapper<VendorProposal>() {
        @Override
        public VendorProposal mapRow(ResultSet resultSet, int i) throws SQLException {
            VendorProposal vendorProposal = new VendorProposal();
            vendorProposal.setId(resultSet.getInt("id"));
            vendorProposal.setVendor(userRepository.findByUserId(resultSet.getInt("vendor_id")));
            vendorProposal.setProductBrand(resultSet.getString("product_brand"));
            vendorProposal.setAvailableSizes(resultSet.getString("available_sizes"));
            vendorProposal.setPrice(resultSet.getInt("price"));
            vendorProposal.setProductName(resultSet.getString("product_name"));
            return vendorProposal;
        }
    };

public void add(VendorProposal vendorProposal)
{
    String query = "insert into vendorProposal(vendor_id,product_name,product_brand,available_sizes,price) values(?,?,?,?,?)";
    jdbcTemplate.update(query,vendorProposal.getVendor().getId(),vendorProposal.getProductName(),vendorProposal.getProductBrand(),vendorProposal.getAvailableSizes(),vendorProposal.getPrice());
}
public List<VendorProposal> findByVendorId(int id)
{
    String query = "select * from vendorProposal where vendor_id='"+id+"'";
    return jdbcTemplate.query(query,vendorProposalRowMapper);
}
    public List<VendorProposal> findAll()
    {
        String query = "select * from vendorProposal";
        return jdbcTemplate.query(query,vendorProposalRowMapper);
    }
}
