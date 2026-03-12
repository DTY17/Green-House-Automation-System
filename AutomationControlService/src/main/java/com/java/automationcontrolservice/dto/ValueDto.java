package com.java.automationcontrolservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValueDto {
    private double value;
    private String tempUnit;
    private double humidity;
    private String humidityUnit;

}