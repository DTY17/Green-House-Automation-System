package com.java.cropinventoryservice.service;

import com.java.cropinventoryservice.dto.CropsDto;
import com.java.cropinventoryservice.entity.Crops;
import com.java.cropinventoryservice.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CropsService {
    @Autowired
    private Repository repository;

    public Boolean save(CropsDto dto) {
        try {
            Crops crops = Crops.builder().name(dto.getName()).zone(dto.getZone()).description(dto.getDescription()).build();
            repository.save(crops);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Crops> getInventory() {
        try {
            List<Crops> list = repository.findAll();
            return new ArrayList<>(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean updateCropStatus(String id, CropsDto dto) {
        try {
            Optional<Crops> list = repository.findById(Long.parseLong(id));
            Crops crops;
            if (list.isPresent()) {
                crops = list.get();
            } else {
                throw new RuntimeException("Wrong Id");
            }
            crops.setStatus(dto.getStatus());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
