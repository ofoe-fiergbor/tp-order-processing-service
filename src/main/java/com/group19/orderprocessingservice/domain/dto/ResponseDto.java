package com.group19.orderprocessingservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ResponseDto {
    private String status;
    private String message;
    private Object object;

    public ResponseDto(String status, String message, Object object) {
        this.status = status;
        this.message = message;
        this.object = object;
    }

    public ResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
