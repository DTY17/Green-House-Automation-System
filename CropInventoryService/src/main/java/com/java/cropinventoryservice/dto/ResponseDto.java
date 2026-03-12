package com.java.cropinventoryservice.dto;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Data

public class ResponseDto {
    private String error;
    private Object message;
}
