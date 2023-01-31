package com.maveric.balanceservice.exception;

public class CustomerIDNotFoundExistsException extends RuntimeException{
    public CustomerIDNotFoundExistsException(String message)
    {
        super(message);
    }
}
