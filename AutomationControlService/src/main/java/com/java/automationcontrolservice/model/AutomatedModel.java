package com.java.automationcontrolservice.model;

import com.java.automationcontrolservice.dto.*;
import com.java.automationcontrolservice.service.AutomatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AutomatedModel {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AutomatedService automatedService;

    @Autowired
    private ZoneDto zoneDto;

    @Value("${iot.integration.url}")
    private String iotIntegrationUrl;

    @Value("${zone.integration.url}")
    private String zoneIntegrationUrl;

    public ResponseEntity<String> automate(SensorReadingDto dto) {

        ZoneDto data = getDevice(dto);

        if(data.getMaxTemp()>dto.getTemperature()) {
            setAction("TURN_ON_FAN",data.getZoneName(),data.getDeviceId());
        } else {
            setAction("TURN_OOF_FAN",data.getZoneName(),data.getDeviceId());
        }
        if(data.getMinTemp()>dto.getTemperature()) {
            setAction("TURN_ON_HEATER",data.getZoneName(),data.getDeviceId());
        } else {
            setAction("TURN_OOF_HEATER",data.getZoneName(),data.getDeviceId());
        }
        return null;
    }

    public ZoneDto getDevice(SensorReadingDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(dto.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        System.out.println(dto.getDeviceId());

        ResponseEntity<ZoneDto> response = restTemplate.exchange(
                zoneIntegrationUrl + "/get/{id}",
                HttpMethod.GET,
                entity,
                ZoneDto.class,
                dto.getDeviceId()
        );

        return response.getBody();
    }


    public void setAction (String action,String zone,String deviceId) {
        System.out.println(action);
        ActionDto dto = ActionDto.builder().Zone(zone).Device(deviceId).message(action).build();
        // automatedService.save(dto);
    }

    public String getCommand(SensorReadingDto dto) {
        return automatedService.getCommand(dto);
    }
}
