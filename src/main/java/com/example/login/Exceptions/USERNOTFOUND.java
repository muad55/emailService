package com.example.login.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class USERNOTFOUND extends RuntimeException  {
    public USERNOTFOUND(String utilisateurIntrouvable) {
        super(utilisateurIntrouvable);
    }
}
