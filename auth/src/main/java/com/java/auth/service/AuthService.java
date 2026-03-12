package com.java.auth.service;

import com.java.auth.entity.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    private final RestTemplate restTemplate;

    public AuthService(RestTemplate template) {
        this.restTemplate = template;
    }

    @Autowired
    private com.java.auth.repository.AuthRepository authRepository;
    public boolean checkAuth(String username,String password) {
        com.java.auth.entity.Auth user = authRepository.findByUsername(username);
        if(user==null || !(user.getPassword().equals(password))) {
            return false;
        }
        return true;
    }

    public boolean register(String username, String password) {
        com.java.auth.entity.Auth user = authRepository.findByUsername(username);
        if(user==null) {
            authRepository.save(new Auth(username,password));
            return true;
        }
        return false;
    }
}
