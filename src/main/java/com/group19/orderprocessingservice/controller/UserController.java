package com.group19.orderprocessingservice.controller;

import com.group19.orderprocessingservice.domain.dto.ResponseDto;
import com.group19.orderprocessingservice.domain.dto.SignInDto;
import com.group19.orderprocessingservice.domain.dto.SignUpDto;
import com.group19.orderprocessingservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-processing/user")
public class UserController {

    @Autowired
    UserService userService;

    //sign up
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerNewUser(@RequestBody SignUpDto signUpDto) {
        return new ResponseEntity<>(userService.signup(signUpDto), HttpStatus.OK);
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> userLogin(@RequestBody SignInDto signinDto) {
        return new ResponseEntity<>(userService.signin(signinDto), HttpStatus.OK);
    }
}
