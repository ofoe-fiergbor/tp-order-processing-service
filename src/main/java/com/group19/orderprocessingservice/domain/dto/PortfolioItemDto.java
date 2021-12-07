package com.group19.orderprocessingservice.domain.dto;

import lombok.Data;

@Data
public class PortfolioItemDto {
    private String ticker;
    private int quantity;
}
