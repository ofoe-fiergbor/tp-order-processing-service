package com.group19.orderprocessingservice.domain.model;

import com.group19.orderprocessingservice.enums.UserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long
            id;
    private String
            name,
            username,
            email,
            password;
    @Enumerated(EnumType.STRING)
    private UserRole
            role;
    private Boolean
            enabled = false,
            locked = false;

    public UserEntity(String name,
                      String username,
                      String email,
                      String password,
                      UserRole role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //user authentication and authorization
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(UserRole.USER.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

