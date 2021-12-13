package com.group19.orderprocessingservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePortfolioDto {

    @JsonProperty("userId")
    private long userId;

    private String name;
}
