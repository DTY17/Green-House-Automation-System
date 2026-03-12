package com.java.zonemanagementservice.service;

import com.java.zonemanagementservice.dto.ZOneIotDto;
import com.java.zonemanagementservice.dto.ZoneDto;
import com.java.zonemanagementservice.entity.Zone;
import com.java.zonemanagementservice.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;

@Service
public class ZoneService {
    @Autowired
    private Repository repository;

    @Value("${iot.integration.url}")
    private String iotIntegrationUrl;

    @Autowired
    private RestTemplate restTemplate;

    public boolean save(ZoneDto zone) throws Exception {
        if (zone.getMinTemp() >= zone.getMaxTemp()) {
            throw new IllegalArgumentException("minTemp must be strictly less than maxTemp");
        }

        try {
            String deviceId = registerDevice(zone, zone.getIotAccessToken());
            Zone list = Zone.builder()
                    .zoneName(zone.getZoneName())
                    .minTemp(zone.getMinTemp())
                    .maxTemp(zone.getMaxTemp())
                    .deviceId(deviceId)
                    .build();
            repository.save(list);
            return true;

        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                String newAccessToken = refreshAccessToken(zone.getIotRefreshToken());
                if (newAccessToken == null) {
                    throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Access and refresh tokens expired");
                }

                String deviceId = registerDevice(zone, newAccessToken);
                Zone list = Zone.builder()
                        .zoneName(zone.getZoneName())
                        .minTemp(zone.getMinTemp())
                        .maxTemp(zone.getMaxTemp())
                        .deviceId(deviceId)
                        .build();
                repository.save(list);
                return true;
            } else {
                throw ex;
            }
        }
    }

    private String registerDevice(ZoneDto zone, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        ZOneIotDto iot = new ZOneIotDto(zone.getId().toString(),zone.getZoneName());
        HttpEntity<ZOneIotDto> entity = new HttpEntity<>(iot, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                iotIntegrationUrl + "/devices",
                entity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("deviceId").toString();
        }
        throw new RuntimeException("Device registration failed: " + response.getStatusCode());
    }

    private String refreshAccessToken(String refreshToken) {
        Map<String, String> payload = Map.of("refreshToken", refreshToken);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    iotIntegrationUrl + "/auth/refresh",
                    payload,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().get("accessToken").toString();
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return null;
            }
            throw ex;
        }
        return null;
    }


    public boolean delete(ZoneDto zone) throws  Exception {
        Zone data = repository.getReferenceById(zone.getId());
        repository.delete(data);
        return true;
    }

    public boolean update(ZoneDto zone) throws Exception {
        Zone list = repository.getReferenceById(zone.getId());
        if (zone.getMinTemp() >= zone.getMaxTemp()) {
            throw new IllegalArgumentException("minTemp must be strictly less than maxTemp");
        }
        zone.setMinTemp(zone.getMinTemp());
        zone.setMaxTemp(zone.getMaxTemp());
        repository.save(list);
        return true;
    }

    public Zone findById(ZoneDto zone) throws Exception {
        return repository.getReferenceById(zone.getId());
    }

    public ArrayList<Zone> findAll() {
        return new ArrayList<>(repository.findAll());
    }

    public Map<String, Object> registerDevice(ZoneDto zone, String accessToken, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<ZoneDto> entity = new HttpEntity<>(zone, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    iotIntegrationUrl + "/devices",
                    entity,
                    Map.class
            );

            return response.getBody();

        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                Map<String, String> refreshPayload = Map.of("refreshToken", refreshToken);
                ResponseEntity<Map> refreshResponse = restTemplate.postForEntity(
                        iotIntegrationUrl + "/auth/refresh",
                        refreshPayload,
                        Map.class
                );

                if (refreshResponse.getStatusCode() == HttpStatus.OK && refreshResponse.getBody() != null) {
                    String newAccessToken = refreshResponse.getBody().get("accessToken").toString();

                    headers.setBearerAuth(newAccessToken);
                    HttpEntity<ZoneDto> retryEntity = new HttpEntity<>(zone, headers);

                    ResponseEntity<Map> retryResponse = restTemplate.postForEntity(
                            iotIntegrationUrl + "/devices",
                            retryEntity,
                            Map.class
                    );
                    return retryResponse.getBody();
                } else {
                    throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
                }
            } else {
                throw ex;
            }
        }
    }

    public Zone findByID(String name) {
        return repository.findByDeviceId(name);
    }
}
