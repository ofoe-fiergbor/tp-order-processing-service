package com.group19.orderprocessingservice.domain.repository.auth;

import com.group19.orderprocessingservice.domain.model.auth.AuthenticationToken;
import com.group19.orderprocessingservice.domain.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<AuthenticationToken, Long> {
    AuthenticationToken findByUser(User user);
}
