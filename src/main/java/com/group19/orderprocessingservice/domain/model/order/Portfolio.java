package com.group19.orderprocessingservice.domain.model.order;

import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.enums.PortfolioStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "portfolios")
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

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private User user;

    private String name;

//    @OneToMany(targetEntity = Order.class, cascade = CascadeType.ALL)
//    @JoinColumn(updatable = false)
//    private List<Order> orderList;


    public Portfolio(User user, String name) {
        this.user = user;
        this.portfolioStatus = PortfolioStatus.ACTIVE;
        this.name = name;
    }
}
