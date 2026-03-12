package com.java.sensortelemetservice.service;

import com.java.sensortelemetservice.dto.SensorReadingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensoryService {


    @Autowired
    private SensorReadingDto latestReading;

    public SensorReadingDto getLatestReading() {
        return latestReading;
    }

    public void updateReading(SensorReadingDto reading) {
        this.latestReading = reading;
    }
}
