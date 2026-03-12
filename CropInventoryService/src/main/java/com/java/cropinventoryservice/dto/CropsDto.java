package com.java.cropinventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropsDto {
    private Long id;
    private String name;
    private String status;
    private String description;
    private Date creationDate;
    private String zone;
}
