package com.java.zonemanagementservice.controller;

import com.java.zonemanagementservice.dto.ResponseDto;
import com.java.zonemanagementservice.dto.ZoneDto;
import com.java.zonemanagementservice.entity.Zone;
import com.java.zonemanagementservice.model.ZoneModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/v1/api")
public class ZoneController {
    @Autowired
    private ZoneModel  zoneModel;

    @Autowired
    private ResponseDto  responseDto;

    @GetMapping
    public ResponseEntity<ResponseDto> getZone () {
        try {
            ArrayList<Zone> list = zoneModel.findAll();
            responseDto.setMessage(list);
            responseDto.setError("none");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setMessage("Error occurred");
            responseDto.setError(e.getMessage());
            return ResponseEntity.status(500).body(responseDto);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> setZone (@RequestBody ZoneDto dto) {
        try {
            boolean isSaved = zoneModel.save(dto);
            if (isSaved) {
                responseDto.setMessage("saved successfully");
                responseDto.setError("none");
                return ResponseEntity.ok(responseDto);
            } else {
                responseDto.setMessage("not saved successfully");
                responseDto.setError("Error occurred");
                return ResponseEntity.status(500).body(responseDto);
            }
        } catch (Exception e) {
            responseDto.setMessage("Error occurred");
            responseDto.setError(e.getMessage());
            return ResponseEntity.status(500).body(responseDto);
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<ResponseDto> deleteZone (@RequestBody ZoneDto dto) {
        try {
            boolean isDelete = zoneModel.delete(dto);
            if (isDelete) {
                responseDto.setMessage("deleted successfully");
                responseDto.setError("none");
                return ResponseEntity.ok(responseDto);
            } else {
                responseDto.setMessage("not deleted successfully");
                responseDto.setError("Error occurred");
                return ResponseEntity.status(500).body(responseDto);
            }
        } catch (Exception e) {
            responseDto.setMessage("Error occurred");
            responseDto.setError(e.getMessage());
            return ResponseEntity.status(500).body(responseDto);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateZone (@RequestBody ZoneDto dto) {
        try {
            boolean isUpdate = zoneModel.update(dto);
            if (isUpdate) {
                responseDto.setMessage("updated successfully");
                responseDto.setError("none");
                return ResponseEntity.ok(responseDto);
            } else {
                responseDto.setMessage("not updated successfully");
                responseDto.setError("Error occurred");
                return ResponseEntity.status(500).body(responseDto);
            }
        } catch (Exception e) {
            responseDto.setMessage("Error occurred");
            responseDto.setError(e.getMessage());
            return ResponseEntity.status(500).body(responseDto);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Zone> getZone (@PathVariable String id) {
        try {
            Zone list = zoneModel.findByID(id);
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            responseDto.setMessage("Error occurred");
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }

    }
}
