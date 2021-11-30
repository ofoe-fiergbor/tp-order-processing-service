package com.group19.orderprocessingservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
//captures few things from client request
public class RegistrationRequest {

    private final String name;
    private final String username;
    private final String email;
    private final String password;
}

