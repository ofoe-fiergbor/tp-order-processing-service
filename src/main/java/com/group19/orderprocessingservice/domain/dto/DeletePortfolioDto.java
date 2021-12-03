package com.group19.orderprocessingservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletePortfolioDto {
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("portfolioId")
    private Long portfolioId;
}
