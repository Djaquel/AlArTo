package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Member;
import fr.eni.auctionapp.bo.PasswordResetToken;

import java.util.Locale;
import java.util.Optional;

public interface SecurityService {

    void sendResetPasswordToken(Locale locale, Member member);

    Optional<PasswordResetToken> getResetTokenByTokenString(String token);

    boolean isValidToken(String token, int memberId);
}
