package com.java.auth.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthModel {
    @Autowired
    private com.java.auth.service.AuthService service;

    public boolean authCheck(String username,String password) {
        return service.checkAuth(username, password);
    }

    public boolean authRegister(String username, String password) {
        return service.register(username,password);
    }
}
