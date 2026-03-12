package com.java.cropinventoryservice.model;

import com.java.cropinventoryservice.dto.CropsDto;
import com.java.cropinventoryservice.entity.Crops;
import com.java.cropinventoryservice.service.CropsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CropsModel {
    @Autowired
    private CropsService cropsService;

    public Boolean save(CropsDto dto) throws Exception {
        return cropsService.save(dto);
    }

    public ArrayList<Crops> getInventory() throws Exception{
        return cropsService.getInventory();
    }

    public Boolean updateCropStatus(String id, CropsDto dto) {
        return cropsService.updateCropStatus(id,dto);
    }
}
