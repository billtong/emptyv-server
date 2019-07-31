package com.empty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;

@Configuration
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendActivateEmail(String email, String link, String username) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String text = MessageFormat.format("<p>Hi {0},<br /><br /></p><p>Welcome to Empty Video!</p><p>Please click the link below to complete the email verification.&nbsp;&nbsp;</p><p><a href=\"{1}\">Verification</a>&nbsp;<br /><br /><br /><br />THANK YOU!<br /> <br />--<br /> Your friends at EmptyVideo</p>", username, link);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setFrom(MessageFormat.format("EmptyVideo <{0}>", this.username));
            helper.setSubject("Email Verification of your new EmptyVideo account");
            helper.setText(text, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }

}
