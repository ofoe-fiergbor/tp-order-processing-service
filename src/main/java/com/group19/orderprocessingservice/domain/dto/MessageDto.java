package com.group19.orderprocessingservice.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MessageDto {
    private String id;
    private String message;
    private Date timestamp;


    public MessageDto(String message) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.timestamp = new Timestamp(new Date().getTime());
    }
}
