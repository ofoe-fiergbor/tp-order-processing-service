package com.group19.orderprocessingservice.domain.model.order;

import com.group19.orderprocessingservice.domain.model.auth.User;
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
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private PortfolioStatus portfolioStatus;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public Portfolio(User user) {
        this.user = user;
        this.portfolioStatus = PortfolioStatus.ACTIVE;
    }
}
