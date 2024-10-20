package com.example.login.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FALSEPASSWORD extends RuntimeException  {
    public FALSEPASSWORD(String motDePasseIncorrect) {
        super(motDePasseIncorrect);
    }
}
