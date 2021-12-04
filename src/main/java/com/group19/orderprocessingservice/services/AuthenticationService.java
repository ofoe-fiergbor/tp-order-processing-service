package com.group19.orderprocessingservice.services;

import com.group19.orderprocessingservice.domain.model.auth.AuthenticationToken;
import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.repository.auth.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    TokenRepository tokenRepository;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepository.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return tokenRepository.findByUser(user);
    }
}
