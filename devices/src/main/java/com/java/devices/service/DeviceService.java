package com.java.devices.service;

import com.java.devices.entity.Device;
import com.java.devices.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private Repository repository;

    public Boolean saveDevice(Device device) {
        try {
            System.out.println("saveDevice");
            repository.save(device);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Device> getDevices() {
        return repository.findAll();
    }

    public Device getDeviceById(Long id) {
        return repository.findAllByIdEquals(id).get();
    }
}
