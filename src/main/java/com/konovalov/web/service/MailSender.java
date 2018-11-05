package com.konovalov.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//Сервис отправки сообщений на имейл
@PropertySource("classpath:mail.properties")
@Service
public class MailSender {
    private final JavaMailSender mail;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    public MailSender(JavaMailSender mailSender) {
        this.mail = mailSender;
    }

    // метод отправки
    void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mail.send(mailMessage);
    }
}
