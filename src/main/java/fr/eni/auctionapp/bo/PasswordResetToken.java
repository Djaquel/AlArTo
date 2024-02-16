package fr.eni.auctionapp.bo;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PasswordResetToken {
    private static final int EXPIRATION = 5;
    private int id;
    private String token;
    private Member member;
    private Timestamp expiry;

    public PasswordResetToken(String token, Member member) {
        this.token = token;
        this.member = member;

        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(EXPIRATION);
        this.expiry = Timestamp.valueOf(dateTime);
    }

    public PasswordResetToken(int id, String token, Member member, Timestamp expiry) {
        this.id = id;
        this.token = token;
        this.member = member;
        this.expiry = expiry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Timestamp getExpiry() {
        return expiry;
    }

    public void setExpiry(Timestamp expiry) {
        this.expiry = expiry;
    }
}
