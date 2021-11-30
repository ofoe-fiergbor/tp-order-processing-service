package com.group19.orderprocessingservice.controller;

import com.group19.orderprocessingservice.domain.model.RegistrationRequest;
import com.group19.orderprocessingservice.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-processing")

public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @PostMapping("/register")
    public String register(@RequestBody RegistrationRequest registration) {
        return registrationService.register(registration);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }


}

