package com.java.sensortelemetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class SensorReadingDto {
    private Double temperature;
    private Double humidity;
    private LocalDateTime timestamp;
    private String accessToken;
    private String refreshToken;
    private String deviceId;
}
