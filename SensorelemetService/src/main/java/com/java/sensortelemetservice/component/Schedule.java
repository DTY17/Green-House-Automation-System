package com.java.sensortelemetservice.component;

import com.java.sensortelemetservice.dto.*;
import com.java.sensortelemetservice.service.SensoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
public class Schedule {

    @Value("${iot.integration.url}")
    private String iotIntegrationUrl;

    @Value("${automation.service.url}")
    private String automationServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SensoryService sensoryService;

    private SensorReadingDto latestReading;
    private DeviceDataDto deviceDataDto;

    private SensoryRequestDto sensoryRequestDto;
    private boolean isAutoAuth = false;

    public void fetchAndPushTelemetry(SensoryRequestDto dto) {
        try {
            if (isAutoAuth) {
                sensoryRequestDto = dto;
            } else {
                sensoryRequestDto = dto;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(sensoryRequestDto.getIotAccessToken());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<DeviceDataDto> response = restTemplate.exchange(
                    iotIntegrationUrl + "/devices/telemetry/{deviceId}",
                    HttpMethod.GET,
                    entity,
                    DeviceDataDto.class,
                    sensoryRequestDto.getDeviceId()
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                DeviceDataDto body = response.getBody();

                latestReading = SensorReadingDto.builder()
                        .temperature(body.getValue().getTemperature())
                        .humidity(body.getValue().getHumidity())
                        .timestamp(body.getCapturedAt()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime())
                        .accessToken(sensoryRequestDto.getAccessToken())
                        .refreshToken(sensoryRequestDto.getRefreshToken())
                        .build();
            }
            latestReading.setDeviceId(sensoryRequestDto.getDeviceId());

            System.out.println("1st part : "+latestReading.getTemperature());
            HttpHeaders pushHeaders = new HttpHeaders();
            pushHeaders.setContentType(MediaType.APPLICATION_JSON);
            pushHeaders.setBearerAuth(sensoryRequestDto.getAccessToken());

            HttpEntity<SensorReadingDto> pushEntity = new HttpEntity<>(latestReading, pushHeaders);
            restTemplate.postForEntity(automationServiceUrl, pushEntity, Void.class);

        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                isAutoAuth = true;

                String newAccessToken = refreshAccessToken(sensoryRequestDto.getRefreshToken());
                if (newAccessToken != null) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    Map<String, String> body = new HashMap<>();
                    body.put("refreshToken", sensoryRequestDto.getRefreshToken());

                    HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

                    ResponseEntity<TokenReposneDto> response = restTemplate.postForEntity(
                            iotIntegrationUrl + "/auth/refresh",
                            entity,
                            TokenReposneDto.class
                    );

                    TokenReposneDto tokenResponse = response.getBody();
                    sensoryRequestDto.setAccessToken(tokenResponse.getAccessToken());
                    sensoryRequestDto.setRefreshToken(tokenResponse.getRefreshToken());

                    HttpHeaders headersRe = new HttpHeaders();
                    headersRe.setBearerAuth(sensoryRequestDto.getAccessToken());
                    HttpEntity<Void> entityRe = new HttpEntity<>(headersRe);

                    ResponseEntity<DeviceDataDto> retryResponse = restTemplate.exchange(
                            iotIntegrationUrl + "/devices/telemetry/{deviceId}",
                            HttpMethod.GET,
                            entityRe,
                            DeviceDataDto.class,
                            sensoryRequestDto.getDeviceId()
                    );

                    if (retryResponse.getStatusCode() == HttpStatus.OK && retryResponse.getBody() != null) {
                        DeviceDataDto bodyRe = retryResponse.getBody();

                        latestReading = SensorReadingDto.builder()
                                .temperature(bodyRe.getValue().getTemperature())
                                .humidity(bodyRe.getValue().getHumidity())
                                .timestamp(bodyRe.getCapturedAt()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime())
                                .accessToken(sensoryRequestDto.getAccessToken())
                                .refreshToken(sensoryRequestDto.getRefreshToken())
                                .build();
                        latestReading.setDeviceId(sensoryRequestDto.getDeviceId());
                    }

                    HttpHeaders pushHeaders = new HttpHeaders();
                    pushHeaders.setContentType(MediaType.APPLICATION_JSON);
                    pushHeaders.setBearerAuth(sensoryRequestDto.getAccessToken());

                    HttpEntity<SensorReadingDto> pushEntityRe = new HttpEntity<>(latestReading, pushHeaders);
                    ResponseEntity<Void> response_au = restTemplate.exchange(
                            automationServiceUrl,
                            HttpMethod.POST,
                            pushEntityRe,
                            Void.class
                    );
                    System.out.println("2nd part : "+latestReading.getTemperature());

                } else {
                    HttpHeaders pushHeaders = new HttpHeaders();
                    pushHeaders.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<TokenReposneDto> loginEntity = new HttpEntity<>(
                            TokenReposneDto.builder()
                                    .username(sensoryRequestDto.getUsername())
                                    .password(sensoryRequestDto.getPassword())
                                    .build(),
                            pushHeaders
                    );

                    ResponseEntity<TokenReposneDto> loginResponse = restTemplate.postForEntity(
                            "http://localhost:8080/auth/v1/api/login",
                            loginEntity,
                            TokenReposneDto.class
                    );

                    System.out.println("3rd part : "+latestReading.getTemperature());
                    TokenReposneDto loginBody = loginResponse.getBody();
                    sensoryRequestDto.setAccessToken(loginBody.getAccessToken());
                    sensoryRequestDto.setRefreshToken(loginBody.getRefreshToken());
                }
            } else {
                throw ex;
            }
        }
    }

    private String refreshAccessToken(String refreshToken) {
        Map<String, String> payload = Map.of("refreshToken", refreshToken);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                iotIntegrationUrl + "/auth/refresh",
                payload,
                Map.class
        );
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("accessToken").toString();
        }
        Map<String, String> data = login(sensoryRequestDto.getUsername(), sensoryRequestDto.getPassword());
        return data.get("refreshToken");
    }

    public Map<String, String> login(String username, String password) {
        Map<String, String> payload = Map.of(
                "username", username,
                "password", password
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                iotIntegrationUrl + "/auth/login",
                payload,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return Map.of(
                    "username", response.getBody().get("username").toString(),
                    "accessToken", response.getBody().get("accessToken").toString(),
                    "refreshToken", response.getBody().get("refreshToken").toString()
            );
        }
        throw new RuntimeException("Login failed: " + response.getStatusCode());
    }
}
