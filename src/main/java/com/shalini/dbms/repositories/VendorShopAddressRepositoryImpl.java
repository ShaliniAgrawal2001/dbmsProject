package com.shalini.dbms.repositories;

import com.shalini.dbms.models.ProductsInCart;
import com.shalini.dbms.models.VendorProposal;
import com.shalini.dbms.models.VendorShopAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VendorShopAddressRepositoryImpl implements VendorShopAddressRepository {
    JdbcTemplate jdbcTemplate;
    UserRepository userRepository;

    @Autowired
    public VendorShopAddressRepositoryImpl(JdbcTemplate jdbcTemplate,UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    private RowMapper<VendorShopAddress> vendorShopAddressRowMapper= new RowMapper<VendorShopAddress>() {
        @Override
        public VendorShopAddress mapRow(ResultSet resultSet, int i) throws SQLException {
            VendorShopAddress vendorShopAddress = new VendorShopAddress();
            vendorShopAddress.setId(resultSet.getInt("id"));
            vendorShopAddress.setVendor(userRepository.findByUserId(resultSet.getInt("vendor_id")));
            vendorShopAddress.setBuildingName(resultSet.getString("building_name"));
            vendorShopAddress.setShopName(resultSet.getString("shop_name"));
            vendorShopAddress.setShopNo(resultSet.getString("shop_no"));
            vendorShopAddress.setLandmark(resultSet.getString("landmark"));
            vendorShopAddress.setCity(resultSet.getString("city"));
            vendorShopAddress.setState(resultSet.getString("state"));
            return vendorShopAddress;
        }
    };

    public void add(VendorShopAddress vendorShopAddress)
    {
        String query="insert into vendorShopAddress(vendor_id,shop_name,shop_no,building_name,landmark,city,state) values(?,?,?,?,?,?,?)";
        jdbcTemplate.update(query,vendorShopAddress.getVendor().getId(),vendorShopAddress.getShopName(),vendorShopAddress.getShopNo(),vendorShopAddress.getBuildingName(),vendorShopAddress.getLandmark(),vendorShopAddress.getCity(),vendorShopAddress.getState());

    }

    public VendorShopAddress findByVendorId(int id)
    {
        String query = "select * from vendorShopAddress where vendor_id='"+id+"'";
        try {

            return jdbcTemplate.queryForObject(query, vendorShopAddressRowMapper);
        }        catch (EmptyResultDataAccessException e) {
            return null;}
    }

    public void update(VendorShopAddress vendorShopAddress)
    {
        String query = "update vendorShopAddress set shop_name='"+vendorShopAddress.getShopName()+"', shop_no='"+vendorShopAddress.getShopNo()+"', building_name='"+vendorShopAddress.getBuildingName()+"', landmark='"+vendorShopAddress.getLandmark()+"', city='"+vendorShopAddress.getCity()+"', pincode='"+"' where vendor_id='"+vendorShopAddress.getVendor().getId()+"'";
        jdbcTemplate.update(query);
    }
}

