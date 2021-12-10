package com.group19.orderprocessingservice.exceptions;

public class CustomException extends IllegalArgumentException{
    public CustomException(String s) {
        super(s);
    }
}
