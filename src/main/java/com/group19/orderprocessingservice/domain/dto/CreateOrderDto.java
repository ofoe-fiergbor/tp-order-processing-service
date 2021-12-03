package com.group19.orderprocessingservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group19.orderprocessingservice.enums.Side;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDto {

    @JsonProperty("product")
    private String product;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("side")
    private Side side;

    @JsonProperty("price")
    private double price;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("portfolioId")
    private Long portfolioId;
}
