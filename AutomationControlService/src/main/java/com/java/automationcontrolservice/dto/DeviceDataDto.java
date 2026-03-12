package com.java.automationcontrolservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDataDto {
    private String deviceId;
    private String zoneId;
    private ValueDto values;
    private Date capturedAt;
}

