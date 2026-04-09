package com.java.auth.controller;

import com.java.auth.dto.ResponseDto;
import com.java.auth.dto.TokenDto;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/v1/api")
public class AuthController {
    @Autowired
    private com.java.auth.component.JwtUtil jwtUtil;

    @Autowired
    private com.java.auth.model.AuthModel authModel;

    @Autowired
    private ObjectProvider<ResponseDto> myBeanProvider;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenDto tokenDto;

    @Value("${iot.integration.url}")
    private String iotIntegrationUrl;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        boolean isValid = authModel.authCheck(username, password);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtUtil.generateToken(username);
        String newTokenRefresh = jwtUtil.generateTokenRefresh(username);

        Map<String, String> payload = Map.of(
                "username", username,
                "password", password
        );

        ResponseEntity<TokenDto> response = restTemplate.postForEntity(
                iotIntegrationUrl + "/auth/login", payload, TokenDto.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            tokenDto = response.getBody();
        } else {
            throw new RuntimeException("Registration failed: " + response.getStatusCode());
        }

        ResponseDto responseDto = myBeanProvider.getObject();
        responseDto.setIotToken(tokenDto);
        responseDto.setUsername(username);
        responseDto.setToken(token);
        responseDto.setRefreshToken(newTokenRefresh);

        return ResponseEntity.ok(responseDto) ;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        boolean isValid = authModel.authRegister(username, password);
        if (!isValid) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto("Registration failed: user already exists"));
        }

        Map<String, String> payload = Map.of(
                "username", username,
                "password", password
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                iotIntegrationUrl + "/auth/register",
                payload,
                Map.class
        );

        if ((response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED)|| response.getBody() == null) {
            System.out.println(response.getStatusCode());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto("IoT API login failed"));
        }

        String accessToken = response.getBody().get("accessToken").toString();
        String refreshToken = response.getBody().get("refreshToken").toString();
        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

        ResponseDto responseDto = myBeanProvider.getObject();
        responseDto.setIotToken(tokenDto);
        responseDto.setUsername(username);
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDto> refreshToken(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String token = credentials.get("token");
        System.out.println(token);
        boolean isValid = jwtUtil.validateTokenRefresh(username,token);
        if (!isValid) {
            System.out.println("is valid false");
            ResponseDto responseDto = myBeanProvider.getObject();
            responseDto.setToken("Invalid refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }

        String newToken = jwtUtil.generateToken(username);
        ResponseDto responseDto = myBeanProvider.getObject();
        responseDto.setToken(newToken);
        return ResponseEntity.ok(responseDto);
    }
}
