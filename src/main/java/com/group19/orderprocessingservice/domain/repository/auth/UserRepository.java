package com.group19.orderprocessingservice.domain.repository.auth;

import com.group19.orderprocessingservice.domain.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

}
