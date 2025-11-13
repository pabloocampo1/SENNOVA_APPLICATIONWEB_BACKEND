package com.example.sennova.web.exception;

public class CustomValidationException extends RuntimeException{
    public CustomValidationException(String message){
      super(message);
    }
}
