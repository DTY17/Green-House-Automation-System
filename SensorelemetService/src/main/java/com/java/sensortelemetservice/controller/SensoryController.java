package com.java.sensortelemetservice.controller;

import com.java.sensortelemetservice.component.DynamicScheduler;
import com.java.sensortelemetservice.component.Schedule;
import com.java.sensortelemetservice.dto.ResponseDto;
import com.java.sensortelemetservice.dto.SensoryRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class SensoryController {
    @Autowired
    private DynamicScheduler dynamicScheduler;

    @Autowired
    private Schedule schedule;

    @GetMapping("latest")
    public RequestEntity<ResponseDto> getLatest() {
        return null;
    }

    @GetMapping("start")
    public ResponseEntity<String> getLatest(@RequestBody SensoryRequestDto dto) {
        if(dto.getStart()){
            dto.setRefreshToken(schedule.login(dto.getUsername(), dto.getPassword()).get("refreshToken"));
            dynamicScheduler.start(() -> schedule.fetchAndPushTelemetry(dto), 10000);
            return ResponseEntity.ok("Service Started");
        } else {
            dynamicScheduler.stop();
            return ResponseEntity.ok("Service Ended");
        }
    }
}
