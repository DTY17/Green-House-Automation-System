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
public class ValueDto {
    private Double temperature;
    private String tempUnit;
    private Double humidity;
    private String humidityUnit;
}
