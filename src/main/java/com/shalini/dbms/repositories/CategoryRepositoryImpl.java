package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    JdbcTemplate jdbcTemplate;
    private RowMapper<Category> categoryRowMapper = new RowMapper<Category>() {
        @Override
        public Category mapRow(ResultSet resultSet, int i) throws SQLException {
            Category category = new Category();
            category.setId(resultSet.getInt("id"));
            category.setName(resultSet.getString("name"));
            category.setImage(resultSet.getString("image"));
            return category;
        }
    };

    @Autowired
    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Category> findAll() {
        String sqlQuery = "select * from category";
        List<Category> categories = jdbcTemplate.query(sqlQuery, categoryRowMapper);
        return categories;
    }
    @Override
    public void addCategory(Category category) {
        String sqlQuery = "insert into category(name,image) values(?,?)";
        jdbcTemplate.update(sqlQuery,category.getName(),category.getImage());
    }
    public Category findById(int id)
    {
        String query = "select * from category where id='"+id+"'";
        Category category = jdbcTemplate.queryForObject(query,categoryRowMapper);
        return category;
    }

}
