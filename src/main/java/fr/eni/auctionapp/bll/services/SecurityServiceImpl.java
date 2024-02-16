package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Member;
import fr.eni.auctionapp.bo.PasswordResetToken;
import fr.eni.auctionapp.dal.ResetTokenDAO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class SecurityServiceImpl implements SecurityService {

    ResetTokenDAO resetTokenDAO;
    EmailService emailService;
    MemberService memberService;

    public SecurityServiceImpl(ResetTokenDAO resetTokenDAO, EmailService emailService, MemberService memberService) {
        this.resetTokenDAO = resetTokenDAO;
        this.emailService = emailService;
        this.memberService = memberService;
    }

    @Override
    public void sendResetPasswordToken(Locale locale, Member member) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, member);
        resetTokenDAO.insert(resetToken);
        SimpleMailMessage message = emailService.sendResetTokenMessage(locale, resetToken.getToken(), member);
    }

    @Override
    public Optional<PasswordResetToken> getResetTokenByTokenString(String token) {
        Optional<PasswordResetToken> resetTokenOpt = resetTokenDAO.findByToken(token);

        if (resetTokenOpt.isEmpty()) {
            return Optional.empty();
        }

        PasswordResetToken resetToken = resetTokenOpt.get();
        if (!resetToken.getExpiry().after(Timestamp.valueOf(LocalDateTime.now()))) {
            return Optional.empty();
        }

        return resetTokenOpt;
    }

    @Override
    public boolean isValidToken(String token, int memberId) {
        Optional<PasswordResetToken> resetTokenOpt = getResetTokenByTokenString(token);
        return resetTokenOpt.filter(resetToken -> resetToken.getMember().getId() == memberId).isPresent();
    }
}
