package com.java.devices.model;

import com.java.devices.dto.DeviceDto;
import com.java.devices.entity.Device;
import com.java.devices.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;

@Component
public class DeviceModel {
    @Autowired
    private DeviceService deviceService;

    public boolean saveDevice(DeviceDto deviceDto) {
        Device device = Device.builder()
                .deviceName(deviceDto.getDeviceName())
                .userID(deviceDto.getUserID())
                .zoneId(deviceDto.getZoneId())
                .build();
        return deviceService.saveDevice(device);
    }

    public ArrayList<Device> getAllDevices() {
        return new ArrayList<Device>(deviceService.getDevices());
    }

    public Device getDevice(DeviceDto deviceDto) {
        return deviceService.getDeviceById(deviceDto.getDeviceId());
    }
}
