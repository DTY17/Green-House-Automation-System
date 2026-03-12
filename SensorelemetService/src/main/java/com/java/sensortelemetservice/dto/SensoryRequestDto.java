package com.java.sensortelemetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensoryRequestDto {
    private String accessToken;
    private String refreshToken;
    private String IotAccessToken;
    private String IotRefreshToken;
    private Boolean start;
    private String deviceId;
    private String username;
    private String password;
}
