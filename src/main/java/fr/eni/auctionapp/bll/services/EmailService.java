package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Member;
import org.springframework.mail.SimpleMailMessage;

import java.util.Locale;

public interface EmailService {
    SimpleMailMessage sendMessage(String to, String subject, String text);

    SimpleMailMessage sendResetTokenMessage(Locale locale, String token, Member member);

    SimpleMailMessage sendWelcomeMessage(Locale locale, Member member);
}
