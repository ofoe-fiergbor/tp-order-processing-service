package com.group19.orderprocessingservice.domain.dto;

import com.group19.orderprocessingservice.domain.model.order.Order;
import com.group19.orderprocessingservice.enums.PortfolioStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDetailsDto {

    private long id;

    private Date createdAt;

    private PortfolioStatus portfolioStatus;

    private long userId;

    private String name;

    List<Order> orders;

    List<StockProduct> products;


}
