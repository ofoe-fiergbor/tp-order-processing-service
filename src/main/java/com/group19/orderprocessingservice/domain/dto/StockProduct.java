package com.group19.orderprocessingservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockProduct {
    @JsonProperty("TICKER")
    private String ticker;
    @JsonProperty("SELL_LIMIT")
    private int sellLimit;
    @JsonProperty("LAST_TRADED_PRICE")
    private double lastTradedPrice;
    @JsonProperty("MAX_PRICE_SHIFT")
    private double maxPriceShift;
    @JsonProperty("ASK_PRICE")
    private double askPrice;
    @JsonProperty("BID_PRICE")
    private double bidPrice;
    @JsonProperty("BUY_LIMIT")
    private int buyLimit;
}