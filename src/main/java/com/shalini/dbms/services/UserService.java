package com.shalini.dbms.services;

import com.shalini.dbms.models.User;
import com.shalini.dbms.models.VerificationToken;

public interface UserService {
    public void save(User user);
    public User findByUsername(String email);
    public boolean userExists(String email);
    public void createVerificationToken(User user, String token);
    public void enableRegisteredUser(User user);
    public VerificationToken getVerificationToken(String token);
    public User findByUserId(int id);
    public void updateInfo(User user);
}
