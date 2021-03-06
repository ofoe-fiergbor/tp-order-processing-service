package com.group19.orderprocessingservice.domain.model.auth;

import com.group19.orderprocessingservice.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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

    @Column(name = "balance")
    private double balance;

    private Role role;

//    @OneToMany(targetEntity = Portfolio.class, cascade = CascadeType.ALL)
//    @JoinColumn(referencedColumnName = "id")
//    private List<Portfolio> portfolioList;

    public User(String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.balance = 5000.00;
        this.role = role;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }
    public void withdraw(double amount) {
        this.balance -= amount;
    }
}
