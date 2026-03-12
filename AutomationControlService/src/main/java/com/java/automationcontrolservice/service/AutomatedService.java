package com.java.automationcontrolservice.service;

import com.java.automationcontrolservice.dto.ActionDto;
import com.java.automationcontrolservice.dto.SensorReadingDto;
import com.java.automationcontrolservice.entity.Log;
import com.java.automationcontrolservice.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class AutomatedService {
    @Autowired
    private Repository repository;

    public void save(ActionDto dto) {
        repository.save(
                Log.builder().zone(dto.getZone()).device(dto.getDevice()).message(dto.getMessage()).build()
        );
    }

    public String getCommand(SensorReadingDto dto) {
        Log latestLog = repository.findLatestLogByDevice(dto.getDeviceId());
        return latestLog.getMessage();
    }
}
