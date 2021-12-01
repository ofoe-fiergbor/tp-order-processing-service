package com.group19.orderprocessingservice.domain.model;

import com.group19.orderprocessingservice.enums.PortfolioStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", updatable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private PortfolioStatus portfolioStatus;

    @Column
    private long userId;

    public Portfolio(long userId) {
        this.userId = userId;
        this.portfolioStatus = PortfolioStatus.ACTIVE;
    }
}
