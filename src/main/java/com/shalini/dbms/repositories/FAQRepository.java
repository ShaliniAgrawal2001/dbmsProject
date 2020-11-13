package com.shalini.dbms.repositories;

import com.shalini.dbms.models.FAQ;

import java.util.List;

public interface FAQRepository {
    public void addFAQ(FAQ faq);
    public List<FAQ> findAll ();
}
