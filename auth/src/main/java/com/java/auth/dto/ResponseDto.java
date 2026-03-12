package com.java.auth.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ResponseDto {
    private String username;
    private String token;
    private String refreshToken;
    private String error;
    private TokenDto IotToken;

    public ResponseDto() {
    }

    public ResponseDto(String username, String token, String refreshToken, String error, TokenDto iotToken) {
        this.username = username;
        this.token = token;
        this.refreshToken = refreshToken;
        this.error = error;
        IotToken = iotToken;
    }

    public ResponseDto(String ioTApiLoginFailed) {
        this.error = ioTApiLoginFailed;
    }

    public TokenDto getIotToken() {
        return IotToken;
    }

    public void setIotToken(TokenDto iotToken) {
        IotToken = iotToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
