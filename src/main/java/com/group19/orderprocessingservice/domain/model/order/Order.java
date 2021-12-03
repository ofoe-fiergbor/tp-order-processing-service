package com.group19.orderprocessingservice.domain.model.order;

import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.enums.Side;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    private String id;

    @CreationTimestamp
    @Column(name = "createAt", updatable = false)
    private Date createdAt;

    private String product;
    private int quantity;
    private double price;
    private Side side;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    @ManyToOne(targetEntity = Portfolio.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "portfolioId", nullable = false)
    private Portfolio portfolio;

    public Order(String product, int quantity, Side side, double price, User user, Portfolio portfolio) {
        this.product = product;
        this.quantity = quantity;
        this.side = side;
        this.user = user;
        this.price = price;
        this.portfolio = portfolio;
        this.createdAt = new Timestamp(new Date().getTime());
    }
}
