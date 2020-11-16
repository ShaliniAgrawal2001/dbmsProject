package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Order;
import com.shalini.dbms.models.ProductsBoughtByCustomer;
import com.shalini.dbms.models.ProductsInCart;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    JdbcTemplate jdbcTemplate;
    UserRepository userRepository;

    @Autowired
    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate,UserRepository userRepository)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    private RowMapper<Order> orderRowMapper = new RowMapper<Order>() {
        @Override
        public Order mapRow(ResultSet resultSet, int i) throws SQLException {
            Order products = new Order();
            products.setId(resultSet.getInt("id"));
            products.setRecievedFromStore(resultSet.getString("recieved_from_store"));
            products.setTimeOfPurchase(resultSet.getString("time_of_purchase"));
            products.setTotalAmount(resultSet.getInt("total_amount"));
            products.setCustomer(userRepository.findByUserId(resultSet.getInt("customer_id")));
            return products;
        }
    };

    public void addRow(Order order) {

            String query = "insert into orders(customer_id, total_amount, time_of_purchase,recieved_from_store) values(?,?,?,?)";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            jdbcTemplate.update(query, order.getCustomer().getId(),order.getTotalAmount(),dtf.format(now),"NO");
        }

    public List<Order> notRecievedOrder()
    {
        String query = "select * from orders where recieved_from_store='NO'";
        return jdbcTemplate.query(query,orderRowMapper);
    }

    public void markRecieved(int id)
    {
        String query = "update orders set recieved_from_store='YES' where id='"+id+"'";
        jdbcTemplate.update(query);
    }

    public Order getId(String timeOfPurchase)
    {
        String query ="select * from orders where time_of_purchase='"+timeOfPurchase+"'";
        return jdbcTemplate.queryForObject(query,orderRowMapper);
    }

    public List<Order> findAll(int customerId)
    {
        String query = "select * from orders where customer_id='"+customerId+"'";
        return jdbcTemplate.query(query,orderRowMapper);
    }

    public Order findById(int id)
    {
        String query = "select * from orders where id='"+id+"'";
        return jdbcTemplate.queryForObject(query,orderRowMapper);
    }
}

