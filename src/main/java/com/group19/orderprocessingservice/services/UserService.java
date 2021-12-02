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
import java.util.List;
import java.util.UUID;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.enabled;

//@Service
//@AllArgsConstructor
// implements UserDetails interface specifically for spring security
//how we find users ones we try to log in
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

    public  void saveUser(UserEntity user){
        userRepository.save(user);
    }

    ///sign up
    public String signUpUser(UserEntity user) {
        boolean userPresent = userRepository.findUserByEmail(user.getEmail()).isPresent();
        if (userPresent) {

            //TODO if email not confirmed
            if(!user.getEnabled()) {
                // TODO check of attributes are the same and
                var token =  confirmationTokenService.getToken(String.valueOf(user.getId())).get();

                // TODO if email not confirmed send confirmation email.
            }
            throw new IllegalStateException("email already taken");
        }
        //encode password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        //saves user in db
        userRepository.save(user);

        //TODO: send email

        return GenerateAndSaveToken(user);
    }

    //Send Confirmation token
    public String GenerateAndSaveToken(UserEntity user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }


    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findUserByEmail(String userEmail) {
        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(()->new UsernameNotFoundException(String.format("User with %s not found",userEmail)));
    }

    public UserEntity findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException(String.format("User with %s not found", id)));
    }

    public void deleteUserByEmail(String email) {
        userRepository.delete(findUserByEmail(email));
    }

}
