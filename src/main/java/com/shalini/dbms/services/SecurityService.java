package com.shalini.dbms.services;

public interface SecurityService {
    public String findLoggedInUsername();
    public void autoLogin(String username, String password);
    public void notVerified();
}
