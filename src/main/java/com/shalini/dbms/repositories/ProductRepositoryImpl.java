package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Category;
import com.shalini.dbms.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository{
    JdbcTemplate jdbcTemplate;
    CategoryRepository categoryRepository;
    @Autowired
    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate, CategoryRepository categoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryRepository=categoryRepository;
    }
    private RowMapper<Product> productRowMapper = new RowMapper<Product>() {
        @Override
        public Product mapRow(ResultSet resultSet, int i) throws SQLException {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setBrand(resultSet.getString("brand"));
            product.setCategory(categoryRepository.findById(resultSet.getInt("category_id")));
            product.setPrice(resultSet.getInt("price"));
            product.setDiscount(resultSet.getInt("discount"));
            product.setDescription(resultSet.getString("description"));
            return product;
        }
    };
    @Override
    public List<Product> findAll() {
        String sqlQuery = "select * from product";
        List<Product> products = jdbcTemplate.query(sqlQuery, productRowMapper);
        return products;
    }
    public void addProduct(Product product)
    {
        String sqlQuery = "insert into product(name,price,discount,category_id,brand,description) values(?,?,?,?,?,?)";
        jdbcTemplate.update(sqlQuery,product.getName(),product.getPrice(),product.getDiscount(),product.getCategory().getId(),product.getBrand(),product.getDescription());
    }
    public List<Product> findDiscounted(){
        String sqlQuery = "select * from product where discount >0";
        List<Product> products = jdbcTemplate.query(sqlQuery, productRowMapper);
        return products;
    }
    public Product findById(int id)
    {
        String sqlQuery = "select * from product where id='"+id+"'";
        Product product = jdbcTemplate.queryForObject(sqlQuery, productRowMapper);
        return product;
    }
    public List<Product> findByCategoryId(int id)
    {
        String sqlQuery = "select * from product where category_id='"+id+"'";
        List<Product> products = jdbcTemplate.query(sqlQuery, productRowMapper);
        return products;
    }

    public void updateProductInfo(Product product)
    {
        String query = "update product set name='"+product.getName()+"', brand='"+product.getBrand()+"', price='"+product.getPrice()+"', discount='"+product.getDiscount()+"', description='"+product.getDescription()+"' where id='"+product.getId()+"'";
        jdbcTemplate.update(query);
    }
}

