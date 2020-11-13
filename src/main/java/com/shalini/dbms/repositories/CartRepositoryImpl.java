package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Cart;
import com.shalini.dbms.models.Product;
import com.shalini.dbms.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CartRepositoryImpl implements CartRepository {
    JdbcTemplate jdbcTemplate;
    UserRepository userRepository;

    @Autowired
    public CartRepositoryImpl(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    private RowMapper<Cart> cartRowMapper = new RowMapper<Cart>() {
        @Override
        public Cart mapRow(ResultSet resultSet, int i) throws SQLException {
            Cart cart = new Cart();
            cart.setId(resultSet.getInt("id"));
            cart.setTotalPrice(resultSet.getInt("total_price"));
            cart.setCustomer(userRepository.findByUserId(resultSet.getInt("customer_id")));
            return cart;
        }
    };

    public Cart findByCustomerId(int id)
    {
        String sqlQuery = "select * from cart where customer_id='"+id+"'";
        Cart cart = jdbcTemplate.queryForObject(sqlQuery, cartRowMapper);
        return cart;
    }

    public Cart findById(int id)
    {
        String sqlQuery = "select * from cart where id='"+id+"'";
        Cart cart=jdbcTemplate.queryForObject(sqlQuery,cartRowMapper);
        return cart;
    }

    public void addCart(User user)
    {
        String query = "insert into cart(customer_id, total_price) values(?,?)";
        jdbcTemplate.update(query,user.getId(),0);
    }

    public void updateTotalPrice(int id, int price)
    {
        String query = "update cart set total_price = '"+price+"' where id='"+id+"'";
        jdbcTemplate.update(query);
    }


}


