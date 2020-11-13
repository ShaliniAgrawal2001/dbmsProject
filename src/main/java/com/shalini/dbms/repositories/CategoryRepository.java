package com.shalini.dbms.repositories;

import com.shalini.dbms.models.Category;

import java.util.List;

public interface CategoryRepository {
    public void addCategory(Category category);
    public List<Category> findAll();
    public Category findById(int id);

}
