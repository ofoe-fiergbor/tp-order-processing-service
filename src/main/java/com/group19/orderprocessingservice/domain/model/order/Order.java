package com.group19.orderprocessingservice.domain.model.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    private String id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    private String product;
    private int quantity;
    private double price;
    private String side;
    private long userId;
    private long portfolioId;

    public Order(String product, int quantity, String side, long userId, long portfolioId) {
        this.product = product;
        this.quantity = quantity;
        this.side = side;
        this.userId = userId;
        this.portfolioId = portfolioId;
    }
}
