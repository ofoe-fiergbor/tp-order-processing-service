package com.group19.orderprocessingservice.controller;

import com.group19.orderprocessingservice.domain.dto.ResponseDto;
import com.group19.orderprocessingservice.domain.dto.SignInDto;
import com.group19.orderprocessingservice.domain.dto.SignUpDto;
import com.group19.orderprocessingservice.services.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@CrossOrigin("*")
@RestController
@RequestMapping("/order-processing/user")
public class UserController {

    @Autowired
    UserService userService;



    //sign up
    @PostMapping("/register")
    @Operation(summary = "Register a new user.")
    public ResponseEntity<ResponseDto> registerNewUser(@RequestBody SignUpDto signUpDto) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(userService.signup(signUpDto), HttpStatus.OK);
    }

    //login
    @PostMapping("/login")
    @Operation(summary = "Login for an existing user.")
    public ResponseEntity<ResponseDto> userLogin(@RequestBody SignInDto signinDto) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(userService.signIn(signinDto), HttpStatus.OK);
    }

    //get-allUsers endpoint
    @GetMapping("/admin/all-users")
    public ResponseEntity<ResponseDto> fetchAllUsers(){
        return new ResponseEntity<>(userService.fetchAllUsers(),HttpStatus.FOUND);
    }
}
