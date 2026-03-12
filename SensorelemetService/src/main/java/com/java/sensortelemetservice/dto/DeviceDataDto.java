package com.java.sensortelemetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDataDto {
    private String deviceId;
    private String zoneId;
    private String tempUnit;
    private ValueDto value;
    private Instant capturedAt;
}

