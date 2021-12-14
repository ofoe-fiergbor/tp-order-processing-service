package com.group19.orderprocessingservice.services.auth;

import com.group19.orderprocessingservice.domain.dto.ResponseDto;
import com.group19.orderprocessingservice.domain.dto.SignInDto;
import com.group19.orderprocessingservice.domain.dto.SignUpDto;
import com.group19.orderprocessingservice.domain.model.auth.AuthenticationToken;
import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.enums.ResponseDTOStatus;
import com.group19.orderprocessingservice.services.redis.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final
    UserRepository userRepository;

    private final
    AuthenticationService authenticationService;

    private final
    MessagingService messagingService;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationService authenticationService, MessagingService messagingService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.messagingService = messagingService;
    }


    @Transactional
    public ResponseDto signup(SignUpDto sudto) {

        messagingService.saveMessage("User Authentication:: Sign up execution for "+ sudto.getFirstName() + " " + sudto.getLastName());
        //check if user is already present
        if (Objects.nonNull(userRepository.findByEmail(sudto.getEmail()))) {
            messagingService.saveMessage("User Authentication:: Failed to create account. There's already an account with " + sudto.getEmail());
            return new ResponseDto(ResponseDTOStatus.FAILED, "There's already an account with this email.");

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
                encryptPassword,
                sudto.getRole()
        );
        userRepository.save(newUser);
        final AuthenticationToken authenticationToken = new AuthenticationToken(newUser);
        authenticationService.saveConfirmationToken(authenticationToken);

        messagingService.saveMessage("User Authentication:: Account for "+ sudto.getFirstName() + " " + sudto.getLastName()+" created successfully.");
        return new ResponseDto(ResponseDTOStatus.SUCCESS, "user registered successfully!", newUser);
    }

    @Transactional
    public ResponseDto signIn(SignInDto sidto) {
        //find user by email
        messagingService.saveMessage("User Authentication:: Sign up execution for "+ sidto.getEmail());

        User user = userRepository.findByEmail(sidto.getEmail());
        if (Objects.isNull(user)) {
            messagingService.saveMessage("User Authentication:: Failed to sign into account for "+ sidto.getEmail()+ " :: User does not exist.");
            return new ResponseDto(ResponseDTOStatus.FAILED, "User does not exist!");
        }

        //hash password and compare with hashed pw in database
        try{
            if (!user.getPassword().equals(hashPassword(sidto.getPassword().trim()))) {
                messagingService.saveMessage("User Authentication:: Failed to sign into account for "+ sidto.getEmail()+":: Wrong password.");
                return new ResponseDto(ResponseDTOStatus.SUCCESS, "Wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //retrieve token
        AuthenticationToken authenticationToken = authenticationService.getToken(user);

        if (Objects.isNull(authenticationToken)) {
            messagingService.saveMessage("User Authentication:: Failed to sign into account for "+ sidto.getEmail()+":: Token is not present.");
            return new ResponseDto(ResponseDTOStatus.FAILED, "Token is not present");
        }

        messagingService.saveMessage("User Authentication:: Success account login for "+ sidto.getEmail());
        return new ResponseDto(ResponseDTOStatus.SUCCESS, "login successful!", authenticationToken);
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.trim().getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        messagingService.saveMessage("User Authentication:: Password Hashed");
        return hash;
    }

    public ResponseDto fetchAllUsers(){
        List<User> allUsers = userRepository.findAll();
        return
                new ResponseDto(ResponseDTOStatus.SUCCESS, allUsers.isEmpty() ? "No user available at the moment" : "User found", allUsers);
    }
}
