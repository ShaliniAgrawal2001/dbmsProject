package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Feedback;

import java.util.List;

public interface FeedbackRepository {
    public List<Feedback> findAll();
    public void add(Feedback feedback);
}
