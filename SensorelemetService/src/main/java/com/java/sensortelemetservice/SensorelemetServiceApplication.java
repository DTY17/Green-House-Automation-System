package com.java.sensortelemetservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SensorelemetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorelemetServiceApplication.class, args);
    }

}
