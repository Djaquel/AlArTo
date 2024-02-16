package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender emailSender;
    private MessageSource messageSource;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${app.hostname}")
    private String appHost;

    public EmailServiceImpl(JavaMailSender emailSender, MessageSource messageSource) {
        this.emailSender = emailSender;
        this.messageSource = messageSource;
    }

    @Override
    public SimpleMailMessage sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        return message;
    }

    @Override
    public SimpleMailMessage sendResetTokenMessage(Locale locale, String token, Member member) {
        String url = appHost + "/changePassword?token=" + token;
        String message = messageSource.getMessage("forgotten.reset", null,  locale);
        return sendMessage(member.getEmail(), message, message + " \r\n" + url);
    }

    @Override
    public SimpleMailMessage sendWelcomeMessage(Locale locale, Member member) {
        String subject = messageSource.getMessage("mail.signup.subject", null,  locale);
        String content = messageSource.getMessage("mail.signup.content", null,  locale);
        return sendMessage(member.getEmail(), subject, content);
    }
}
