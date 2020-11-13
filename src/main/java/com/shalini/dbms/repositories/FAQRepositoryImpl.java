package com.shalini.dbms.repositories;

import com.shalini.dbms.models.FAQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FAQRepositoryImpl implements FAQRepository {
    JdbcTemplate jdbcTemplate;
    @Autowired
    public FAQRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }
    private RowMapper<FAQ> faqRowMapper = new RowMapper<FAQ>() {
        @Override
        public FAQ mapRow(ResultSet resultSet, int i) throws SQLException {
            FAQ faq = new FAQ();
            faq.setAns(resultSet.getString("ans"));
            faq.setQues(resultSet.getString("ques"));
            return faq;
        }
    };
    public void addFAQ(FAQ faq)
    {
     String query = "insert into faq(ques,ans) values(?,?)";
     jdbcTemplate.update(query,faq.getQues(),faq.getAns());
    }
    public List<FAQ> findAll ()
    {
        String query = "select * from faq";
        return jdbcTemplate.query(query,faqRowMapper);
    }
}
