package com.java.zonemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZoneDto {
    private Long id;
    private String zoneName;
    private double minTemp;
    private double maxTemp;
    private String iotAccessToken;
    private String iotRefreshToken;
}
