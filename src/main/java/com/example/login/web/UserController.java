package com.example.login.web;


import com.example.login.Entities.User;
import com.example.login.Exceptions.FALSEPASSWORD;
import com.example.login.Exceptions.USERNOTFOUND;
import com.example.login.Services.UserServiceImpl;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public User creerCompte(@RequestBody User user){
        return userServiceImpl.register(user);
    }
    @PostMapping("/login")
    public User login(@RequestBody User user) throws USERNOTFOUND, FALSEPASSWORD {
        return userServiceImpl.login(user.getEmail(),user.getPassword());
    }
    @PostMapping("/forgetPassword")
    public void forget(@RequestBody Map<String, String> payload) throws MessagingException,USERNOTFOUND {
        String email = payload.get("email");
        log.info("Recherche de l'utilisateur par email: " + email);
        userServiceImpl.initiatePassword(email);
    }
    @PostMapping("/resetPassword")
    public User Reset(@RequestParam String code,@RequestParam String email,@RequestParam String newPass ){
        return userServiceImpl.checkCodeAndChangePassword(code,email,newPass);
    }

}
