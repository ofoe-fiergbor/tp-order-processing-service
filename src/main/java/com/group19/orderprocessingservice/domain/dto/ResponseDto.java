package com.group19.orderprocessingservice.domain.dto;

import com.group19.orderprocessingservice.enums.ResponseDTOStatus;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ResponseDto {
    private ResponseDTOStatus status;
    private String message;
    private Object object;

    public ResponseDto(ResponseDTOStatus status, String message, Object object) {
        this.status = status;
        this.message = message;
        this.object = object;
    }

    public ResponseDto(ResponseDTOStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
