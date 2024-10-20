package com.example.login.Services;


import com.example.login.Entities.User;
import com.example.login.Exceptions.FALSEPASSWORD;
import com.example.login.Exceptions.USERNOTFOUND;
import com.example.login.Repositories.UserRepo;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

import static com.example.login.PasswordHash.generateHash;
import static com.example.login.PasswordHash.verifyPwd;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SendEmail emailService;

    @Override
    public User register(User user) {
        user.setPassword(generateHash(user.getPassword()));
        User save = userRepo.save(user);
        return save;
    }
    @Override
    public User login(String email, String password) throws USERNOTFOUND, FALSEPASSWORD {
        User user=userRepo.findByEmail(email);
        if(user==null)
            throw new USERNOTFOUND("Utilisateur Introuvable");
        boolean x=verifyPwd(password, user.getPassword());
        if(!x){
            throw new FALSEPASSWORD("Mot de passe incorrect");
        }
        return user;
    }
    @Override
    public void initiatePassword(String email) throws USERNOTFOUND, MessagingException {
        log.info("Recherche de l'utilisateur par email: " + email);
        User user=userRepo.findByEmail(email);
        log.info(String.valueOf(user));
        if(user==null)
            throw new USERNOTFOUND("Utilisateur Introuvable");
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        String verificationCode=String.valueOf(code);
        String hashedCode=generateHash(verificationCode);
        user.setVerificationCode(hashedCode);
        user.setCodeExpirationTime(LocalDateTime.now().plusMinutes(30));
        userRepo.save(user);
        emailService.sendVerificationCode(user.getEmail(), verificationCode, user);
    }
    @Override
    public User checkCodeAndChangePassword(String code, String email, String newPass){
        log.info("code :"+code);
        log.info("email :"+email);
        log.info("newPass :"+newPass);
        User user=userRepo.findByEmail(email);
        if(user==null)
            throw new USERNOTFOUND("Utilisateur Introuvable");
        if (user.getCodeExpirationTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification code has expired.");
        }
        if (!verifyPwd(code, user.getVerificationCode())) {
            throw new IllegalArgumentException("Invalid verification code.");
        }
        String hashedPass=generateHash(newPass);
        user.setPassword(hashedPass);
        user.setVerificationCode(null);
        user.setCodeExpirationTime(null);
        userRepo.save(user);
        return user;
    }
}
