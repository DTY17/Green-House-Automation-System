package com.java.devices.controller;

import com.java.devices.dto.DeviceDto;
import com.java.devices.dto.ResponseDto;
import com.java.devices.entity.Device;
import com.java.devices.model.DeviceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/v1/api")
public class DeviceController {
    @Autowired
    private DeviceModel deviceModel;

    @Autowired
    private ResponseDto responseDto;

    @GetMapping
    public ResponseEntity<ResponseDto> getDevice(@ModelAttribute DeviceDto dto) {
        try {
            System.out.println(dto.getDeviceName()+" "+dto.getZoneId());
            ArrayList<Device> list = deviceModel.getAllDevices();
            responseDto.setMessage(list);
            responseDto.setError("none");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            responseDto.setMessage("error occurred");
            responseDto.setError(e.toString());
            return ResponseEntity.status(500).body(responseDto);
        }

    }

    @PostMapping
    public ResponseEntity<ResponseDto> setDevice(@RequestBody DeviceDto dto) {
        System.out.println(dto.getDeviceName()+" "+dto.getZoneId());
        if (deviceModel.saveDevice(dto)) {
            responseDto.setMessage("Device saved successfully");
            responseDto.setError("none");
            return ResponseEntity.ok(responseDto);
        } else {
            responseDto.setMessage("Device Not saved successfully");
            responseDto.setError("Internal Error");
            return ResponseEntity.status(500).body(responseDto);
        }
    }

    @GetMapping("device-id/{deviceId}")
    public ResponseEntity<ResponseDto> getDeviceById(@PathVariable("deviceId") String deviceId) {
        try {
            DeviceDto dto = new DeviceDto();
            dto.setDeviceId(Long.parseLong(deviceId));
            Device device = deviceModel.getDevice(dto);
            responseDto.setMessage(device);
            responseDto.setError("none");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setMessage("error occurred");
            responseDto.setError(e.toString());
            return ResponseEntity.ok(responseDto);
        }
    }
}
