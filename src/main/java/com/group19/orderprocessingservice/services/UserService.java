package com.group19.orderprocessingservice.services;

import com.group19.orderprocessingservice.domain.model.ConfirmationToken;
import com.group19.orderprocessingservice.domain.model.UserEntity;
import com.group19.orderprocessingservice.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final static String USER_NOT_FOUND_MESSAGE = "user with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String mail)
            throws UsernameNotFoundException {
        return userRepository.findUserByEmail(mail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MESSAGE, mail)));
    }

    public String signUpUser(UserEntity user) {
        boolean userPresent = userRepository.findUserByEmail(user.getEmail()).isPresent();
        if (userPresent) {
            throw new IllegalStateException("email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        //saves user in db
        userRepository.save(user);

        //Send Confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //TODO: send email

        return token;
    }


    public int enableUser(String email) {

        return userRepository.enableUser(email);
    }
}

