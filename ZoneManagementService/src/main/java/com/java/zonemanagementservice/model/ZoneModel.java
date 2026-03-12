package com.java.zonemanagementservice.model;

import com.java.zonemanagementservice.dto.ZoneDto;
import com.java.zonemanagementservice.entity.Zone;
import com.java.zonemanagementservice.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ZoneModel {
    @Autowired
    private ZoneService zoneService;

    public boolean save(ZoneDto zone) throws Exception {
        return zoneService.save(zone);
    }

    public boolean delete(ZoneDto zone) throws Exception {
        return zoneService.delete(zone);
    }

    public boolean update(ZoneDto zone) throws Exception {
        return zoneService.update(zone);
    }
    public Zone findById(ZoneDto zone) throws Exception {
        return zoneService.findById(zone);
    }
    public ArrayList<Zone> findAll() {
        return new ArrayList<>(zoneService.findAll());
    }
    public Zone findByID(String id) {
        return zoneService.findByID(id);
    }
}
