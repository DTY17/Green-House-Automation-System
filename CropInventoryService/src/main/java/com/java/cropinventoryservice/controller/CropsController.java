package com.java.cropinventoryservice.controller;

import com.java.cropinventoryservice.dto.CropsDto;
import com.java.cropinventoryservice.dto.ResponseDto;
import com.java.cropinventoryservice.entity.Crops;
import com.java.cropinventoryservice.model.CropsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("v1/api/crops")
public class CropsController {
    @Autowired
    private CropsModel cropsModel;

    @PostMapping
    public ResponseEntity<ResponseDto> setCrops(CropsDto dto){
        try {
            Boolean isSaved = cropsModel.save(dto);

            ResponseDto responseDto = new ResponseDto();
            responseDto.setError("No Errors");
            responseDto.setMessage("Crops Successfully saved");
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setError(e.getMessage());
            responseDto.setMessage("Error");
            return ResponseEntity.status(500).body(responseDto);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getCurrentInventory(){
        try {
            ArrayList<Crops> list = cropsModel.getInventory();

            ResponseDto responseDto = new ResponseDto();
            responseDto.setError("No Errors");
            responseDto.setMessage(list);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setError(e.getMessage());
            responseDto.setMessage("Error");
            return ResponseEntity.status(500).body(responseDto);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseDto> updateCropsStatus (@PathVariable String id,@RequestBody CropsDto dto) {
        try {
            Boolean isUpdated = cropsModel.updateCropStatus(id,dto);

            ResponseDto responseDto = new ResponseDto();
            responseDto.setError("No Errors");
            responseDto.setMessage("Crop Updated Successfully");
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setError(e.getMessage());
            responseDto.setMessage("Error");
            return ResponseEntity.status(500).body(responseDto);
        }
    }

}
