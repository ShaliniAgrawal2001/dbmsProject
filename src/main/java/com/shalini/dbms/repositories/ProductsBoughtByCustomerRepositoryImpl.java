package com.shalini.dbms.repositories;

import com.shalini.dbms.models.ProductsBoughtByCustomer;
import com.shalini.dbms.models.ProductsInCart;
import com.shalini.dbms.models.User;
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
public class ProductsBoughtByCustomerRepositoryImpl implements ProductsBoughtByCustomerRepository{

    JdbcTemplate jdbcTemplate;
    ProductRepository productRepository;
    UserRepository userRepository;
    OrderRepository orderRepository;
    @Autowired
    public ProductsBoughtByCustomerRepositoryImpl(JdbcTemplate jdbcTemplate,OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private RowMapper<ProductsBoughtByCustomer> productsBoughtByCustomerRowMapper = new RowMapper<ProductsBoughtByCustomer>() {
        @Override
        public ProductsBoughtByCustomer mapRow(ResultSet resultSet, int i) throws SQLException {
            ProductsBoughtByCustomer products = new ProductsBoughtByCustomer();
            products.setId(resultSet.getInt("id"));
            products.setCustomer(userRepository.findByUserId(resultSet.getInt("customer_id")));
            products.setProduct(productRepository.findById(resultSet.getInt("product_id")));
            products.setSize(resultSet.getString("size"));
            products.setOrder(orderRepository.findById(resultSet.getInt("order_id")));
            products.setQuantity(resultSet.getInt("quantity"));
            return products;
        }
    };
public void addRow(List<ProductsInCart> products, int orderId) {
    int i;
    for (i = 0; i < products.size(); i++) {
        String query = "insert into ProductsBoughtByCustomer(product_id, customer_id, quantity, size,order_id) values(?,?,?,?,?)";
        jdbcTemplate.update(query, products.get(i).getProduct().getId(),products.get(i).getCart().getCustomer().getId(), products.get(i).getQty(), products.get(i).getSize(),orderId);
    }
}

public List<ProductsBoughtByCustomer> findByOrderId(int id)
{
    String query = "select * from ProductsBoughtByCustomer where order_id = '"+id+"'";
    List<ProductsBoughtByCustomer> x = jdbcTemplate.query(query, productsBoughtByCustomerRowMapper);
    return (x);
}


}
