package com.java.automationcontrolservice.controller;

import com.java.automationcontrolservice.dto.SensorReadingDto;
import com.java.automationcontrolservice.model.AutomatedModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api")
public class AutomatedController {
    @Autowired
    private AutomatedModel automatedModel;

    @PostMapping
    public ResponseEntity<String> automated(@RequestBody SensorReadingDto dto){
        return automatedModel.automate(dto);
    }

    @GetMapping("command")
    public String getCommand(@RequestBody SensorReadingDto dto){
        return automatedModel.getCommand(dto);
    }
}
