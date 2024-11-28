package com.fitnessapp.management.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String message){super(message);}
}
