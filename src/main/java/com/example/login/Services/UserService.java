package com.example.login.Services;

import com.example.login.Entities.User;
import com.example.login.Exceptions.FALSEPASSWORD;
import com.example.login.Exceptions.USERNOTFOUND;
import jakarta.mail.MessagingException;

public interface UserService {

    public User register(User user);

    User login(String email, String password) throws USERNOTFOUND, FALSEPASSWORD;

    void initiatePassword(String email) throws USERNOTFOUND, MessagingException;

    User checkCodeAndChangePassword(String code, String email, String newPass);
}
