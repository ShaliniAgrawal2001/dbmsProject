package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Inventory;
import com.shalini.dbms.models.ProductsInCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {
    JdbcTemplate jdbcTemplate;
    ProductRepository productRepository;
    @Autowired
    public InventoryRepositoryImpl(JdbcTemplate jdbcTemplate, ProductRepository productRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRepository=productRepository;
    }

    private RowMapper<Inventory> inventoryRowMapper = new RowMapper<Inventory>() {
        @Override
        public Inventory mapRow(ResultSet resultSet, int i) throws SQLException {
            Inventory inventory = new Inventory();
            inventory.setId(resultSet.getInt("id"));
            inventory.setSize(resultSet.getString("size"));
            inventory.setInhouseqty(resultSet.getInt("inhouseqty"));
            inventory.setProduct(productRepository.findById(resultSet.getInt("product_id")));
            return inventory;
        }
    };
    public List<Inventory> findByProductId(int id)
    {
        String sqlQuery = "select * from inventory where product_id='"+id+"' AND inhouseqty>0";
        List<Inventory> inventory = jdbcTemplate.query(sqlQuery, inventoryRowMapper);
        return inventory;
    }
    public int findQuantity(int id, String size)
    {
        String sqlquery = "select * from inventory where product_id='"+id+"' AND size='"+size+"'";
        Inventory inventory = jdbcTemplate.queryForObject(sqlquery,inventoryRowMapper);
        return inventory.getInhouseqty();
    }

    public Inventory findByProductAndSize(int product_id, String size)
    {
        String query = "select * from inventory where product_id='"+product_id+"' AND size='"+size+"'";
        try{
        return (jdbcTemplate.queryForObject(query,inventoryRowMapper));}
        catch (EmptyResultDataAccessException e) {
            return null;}
    }

    public void updateQty(List<ProductsInCart> productsInCart)
    {
        int i;
        String query;
        for(i=0;i<productsInCart.size();i++)
        {
            int qty = findQuantity(productsInCart.get(i).getProduct().getId(),productsInCart.get(i).getSize())-productsInCart.get(i).getQty();
            if(qty>0)
             query = "update inventory set inhouseqty = '"+qty+"' where product_id='"+productsInCart.get(i).getProduct().getId()+"' AND size='"+productsInCart.get(i).getSize()+"'";
            else
                query = "delete from inventory where product_id='"+productsInCart.get(i).getProduct().getId()+"' AND size='"+productsInCart.get(i).getSize()+"'";
            jdbcTemplate.update(query);
        }
    }

    public void updateQtyOfProduct(Inventory inventory)
    {
        String size = inventory.getSize();
        String query;
        int product_id = inventory.getProduct().getId();
        if(inventory.getInhouseqty()>0)
      query = "update inventory set inhouseqty ='"+inventory.getInhouseqty()+"' where size='"+size+"' AND product_id='"+product_id+"'";
        else
           query = "delete from inventory where size='"+size+"' AND product_id='"+product_id+"'";
        jdbcTemplate.update(query);
    }

    public void add(Inventory inventory)
    {
        if(inventory.getInhouseqty()>0){
        String query = "insert into inventory(product_id, size, inhouseqty) values(?,?,?)";
        jdbcTemplate.update(query,inventory.getProduct().getId(),inventory.getSize(),inventory.getInhouseqty());}
    }
}
