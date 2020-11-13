package com.shalini.dbms.repositories;

import com.shalini.dbms.models.User;

public interface UserRepository {
    public User findByEmail(String email);
    public boolean userExists(String email);
    public void save(User user);
    public void enableUser(User user);
    public User findByUserId(int id);
    public void updateData(User user);
}
