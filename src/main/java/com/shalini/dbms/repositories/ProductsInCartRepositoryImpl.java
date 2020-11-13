package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Cart;
import com.shalini.dbms.models.Product;
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
public class ProductsInCartRepositoryImpl implements ProductsInCartRepository{

    JdbcTemplate jdbcTemplate;
    CartRepository cartRepository;
    ProductRepository productRepository;
    @Autowired
    public ProductsInCartRepositoryImpl(JdbcTemplate jdbcTemplate, CartRepository cartRepository,ProductRepository productRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.cartRepository = cartRepository;
        this.productRepository=productRepository;
    }

    private RowMapper<ProductsInCart> productsIncartRowMapper = new RowMapper<ProductsInCart>() {
        @Override
        public ProductsInCart mapRow(ResultSet resultSet, int i) throws SQLException {
            ProductsInCart products = new ProductsInCart();
            products.setId(resultSet.getInt("id"));
            products.setCart(cartRepository.findById(resultSet.getInt("cart_id")));
            products.setProduct(productRepository.findById(resultSet.getInt("product_id")));
            products.setSize(resultSet.getString("size"));
            products.setQty(resultSet.getInt("quantity"));
return products;
        }
    };

    public List<ProductsInCart> findProductsByCartId(int id)
    {
        String sqlQuery = "select * from productsInCart where cart_id='"+id+"'";
        List <ProductsInCart> products= jdbcTemplate.query(sqlQuery,productsIncartRowMapper);
        return products;
    }

    public void addToCart(ProductsInCart productsInCart)
    {
        String query = "insert into productsInCart(cart_id, product_id, size, quantity) values(?,?,?,?)";
                jdbcTemplate.update(query,productsInCart.getCart().getId(),productsInCart.getProduct().getId(),productsInCart.getSize(),productsInCart.getQty());
    }

    public int getTotalPrice(int cartId)
    {
        String query = "select * from productsInCart where cart_id='"+cartId+"'";
        List<ProductsInCart> products = jdbcTemplate.query(query,productsIncartRowMapper);
        int totalPrice=0;
        for(int i=0;i<products.size();i++)
            totalPrice += products.get(i).getQty()*products.get(i).getProduct().getPrice()*(100-products.get(i).getProduct().getDiscount())/100;
        return totalPrice;
    }

    public void removeFromCart(int id)
    {
        String query = "delete from productsInCart where id='"+id+"'";
        jdbcTemplate.update(query);
    }

    public ProductsInCart findBySizeCartIdProductId(String size, int cartId, int productId)
    {
        String query = "select * from productsInCart where size='"+size+"' AND cart_id='"+cartId+"' AND product_id='"+productId+"'";
        try{
        ProductsInCart productsInCart = jdbcTemplate.queryForObject(query,productsIncartRowMapper);
        return productsInCart;}
        catch (EmptyResultDataAccessException e) {
            return null;}
    }
    public void updateQty(int qty, int id)
    {
        String query = "update ProductsInCart set quantity = '"+qty+"' where id='"+id+"'";
        jdbcTemplate.update(query);
    }

    public void emptyCart(int id)
    {
        String query = "delete from productsInCart where cart_id = '"+id+"'";
        jdbcTemplate.update(query);
    }

    public ProductsInCart findById(int id)
    {
        String query = "select * from productsInCart where id = '"+id+"'";
        return(jdbcTemplate.queryForObject(query,productsIncartRowMapper));
    }
}
