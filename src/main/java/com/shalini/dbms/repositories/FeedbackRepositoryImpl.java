package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepository{
    JdbcTemplate jdbcTemplate;
   UserRepository userRepository;
    @Autowired
    public FeedbackRepositoryImpl(JdbcTemplate jdbcTemplate, ProductRepository productRepository, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;

    }
    private RowMapper<Feedback> feedbackRowMapper = new RowMapper<Feedback>() {
        @Override
        public Feedback mapRow(ResultSet resultSet, int i) throws SQLException {
            Feedback feedback = new Feedback();
            feedback.setId(resultSet.getInt("id"));
            feedback.setCustomer(userRepository.findByUserId(resultSet.getInt("customer_id")));
            feedback.setFeedback(resultSet.getString("feedback"));
            return feedback;
        }
    };
    public List<Feedback> findAll()
    {
        String query = "select * from feedback ";
        return jdbcTemplate.query(query,feedbackRowMapper);
    }
    public void add(Feedback feedback)
    {
        String query = "insert into feedback(customer_id, feedback) values(?,?)";
        jdbcTemplate.update(query,feedback.getCustomer().getId(),feedback.getFeedback());
    }
}
