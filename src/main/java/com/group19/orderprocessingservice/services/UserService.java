package com.group19.orderprocessingservice.services;

import com.group19.orderprocessingservice.domain.dto.ResponseDto;
import com.group19.orderprocessingservice.domain.dto.SignInDto;
import com.group19.orderprocessingservice.domain.dto.SignUpDto;
import com.group19.orderprocessingservice.domain.model.auth.AuthenticationToken;
import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.enums.ResponseDTOStatus;
import com.group19.orderprocessingservice.exceptions.AuthenticationFailedException;
import com.group19.orderprocessingservice.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Transactional
    public ResponseDto signup(SignUpDto sudto) {
        //check if user is already present
        if (Objects.nonNull(userRepository.findByEmail(sudto.getEmail()))) {
//            throw new CustomException("There's already an account with this email.");
            return new ResponseDto(ResponseDTOStatus.FAILED.toString(), "There's already an account with this email.");

        }
        //hash password
        String encryptPassword = sudto.getPassword();

        try {
            encryptPassword = hashPassword(sudto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //save user
        User newUser = new User(
                sudto.getFirstName(),
                sudto.getLastName(),
                sudto.getEmail(),
                encryptPassword
        );
        userRepository.save(newUser);
        final AuthenticationToken authenticationToken = new AuthenticationToken(newUser);
        authenticationService.saveConfirmationToken(authenticationToken);
        return new ResponseDto("success", "user registered successfully!", newUser);
    }

    @Transactional
    public ResponseDto signin(SignInDto sidto) {
        //find user by email
        User user = userRepository.findByEmail(sidto.getEmail());
        if (Objects.isNull(user)) {
            throw new AuthenticationFailedException("User does not exist!");
        }
        //hash password and compare with hashed pw in database
        try{
            if (!user.getPassword().equals(hashPassword(sidto.getPassword().trim()))) {
//                throw new AuthenticationFailedException("Wrong password!");
                return new ResponseDto(ResponseDTOStatus.SUCCESS.toString(), "Wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //retrieve token
        AuthenticationToken authenticationToken = authenticationService.getToken(user);

        if (Objects.isNull(authenticationToken)) {
//            throw new CustomException("Token is not present");
            return new ResponseDto(ResponseDTOStatus.FAILED.toString(), "Token is not present");
        }
        return new ResponseDto(ResponseDTOStatus.SUCCESS.toString(), "login successful!", authenticationToken);
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.trim().getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return hash;
    }
}
