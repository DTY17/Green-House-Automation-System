package com.java.sensortelemetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class TokenReposneDto {
    private String username;
    private  String password;
    private String accessToken;
    private String refreshToken;
}
