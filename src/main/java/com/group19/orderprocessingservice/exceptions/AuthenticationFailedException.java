package com.group19.orderprocessingservice.exceptions;

public class AuthenticationFailedException extends  IllegalArgumentException{
    public AuthenticationFailedException(String s) {
        super(s);
    }
}
