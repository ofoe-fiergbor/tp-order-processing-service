package com.group19.orderprocessingservice.domain.model.auth;

import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(targetEntity = Portfolio.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "portfolioId", referencedColumnName = "id")
    private List<Portfolio> portfolioList;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
