package com.java.automationcontrolservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionDto {
    private Long id;
    private Date date;
    private String Zone;
    private String Device;
    private String message;
}
