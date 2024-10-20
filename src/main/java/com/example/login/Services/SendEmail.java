package com.example.login.Services;


import com.example.login.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendEmail {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }
    @Async
    public void sendVerificationCode(String to, String code, User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String body = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; color: #333; line-height: 1.6; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }" +
                ".header { font-size: 20px; font-weight: bold; margin-bottom: 20px; }" +
                ".content { margin-bottom: 20px; }" +
                ".code { font-size: 24px; font-weight: bold; color: #f44336; }" +
                ".footer { font-size: 12px; color: #777; margin-top: 30px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>Code de Vérification</div>" +
                "<div class='content'>" +
                "<p>Bonjour " + user.getUsername() +  ",</p>" +
                "<p>Nous avons reçu une demande pour réinitialiser votre mot de passe. Veuillez utiliser le code de vérification ci-dessous pour continuer :</p>" +
                "<p class='code'>" + code + "</p>" +
                "<p>Ce code expirera dans 30 minutes.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Si vous n'avez pas demandé cela, veuillez ignorer cet email.</p>" +
                "<p>Merci,</p>" +
                "<p>L'équipe de votre entreprise</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        helper.setTo(to);
        helper.setSubject("Code de Vérification");
        helper.setText(body, true);
        mailSender.send(message);
    }


}
