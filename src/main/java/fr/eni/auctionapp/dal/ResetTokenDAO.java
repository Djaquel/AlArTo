package fr.eni.auctionapp.dal;

import fr.eni.auctionapp.bo.PasswordResetToken;

import java.util.Optional;

public interface ResetTokenDAO extends DAO<PasswordResetToken> {
    Optional<PasswordResetToken> findByToken(String token);
}
