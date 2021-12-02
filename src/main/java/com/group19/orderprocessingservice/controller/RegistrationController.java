package com.group19.orderprocessingservice.controller;

import com.group19.orderprocessingservice.domain.model.RegistrationRequest;
import com.group19.orderprocessingservice.domain.model.UserEntity;
import com.group19.orderprocessingservice.exceptions.RegistryException;
import com.group19.orderprocessingservice.services.PasswordService;
import com.group19.orderprocessingservice.services.RegistrationService;
import com.group19.orderprocessingservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-processing")

public class RegistrationController {
    @Autowired
    RegistrationService registrationService;
    @Autowired
    UserService userService;
    @Autowired
    PasswordService loginService;


    ///gets only registered active users
    @GetMapping
    public ResponseEntity<List<UserEntity>> getRegisteredList(){
        return ResponseEntity.ok().body(userService.findAll().stream().filter(UserEntity::getEnabled).toList());
    }

    ///gets only registered active users
    @GetMapping(path="all")
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        return ResponseEntity.ok().body(userService.findAll().stream().toList());
    }

    //registers user
    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registration) throws RegistryException {
        return ResponseEntity.ok().body(registrationService.register(registration));
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) throws RegistryException {
        return registrationService.confirmToken(token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") long userId) {
        userService.deleteUserByEmail(userService.findUserById(userId).getEmail());
        return ResponseEntity.ok().build();
    }



    //custom login and reset password endpoints
    @RequestMapping(value="/try/login", method=RequestMethod.POST)
    public ResponseEntity<String> login(UserEntity user){
        return  ResponseEntity.ok().body(loginService.login(user));
    }
    @RequestMapping(value="/forgot-password", method=RequestMethod.POST)
    public ResponseEntity<String> forgotUserPassword( UserEntity user) {
        return ResponseEntity.ok().body(loginService.forgotUserPassword(user));
    }
    @RequestMapping(value="/confirm-reset", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> validateResetToken(@RequestParam("token")String confirmationToken) {
        return ResponseEntity.ok().body(loginService.validateResetToken(confirmationToken));
    }
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<String> resetUserPassword( UserEntity user) {
        return  ResponseEntity.ok().body(loginService.resetUserPassword(user));
    }
}


