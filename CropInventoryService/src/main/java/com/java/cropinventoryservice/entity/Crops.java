package com.java.cropinventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Crops {
    @Id
    private Long id;
    private String name;
    private String status;
    private String description;
    private Date creationDate;
    private String zone;
}
