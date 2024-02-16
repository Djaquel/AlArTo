package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Auction;
import fr.eni.auctionapp.bo.Member;
import org.springframework.security.core.Authentication;

public interface CreditService {
    boolean hasEnough(Member member, int amount);

    void refound(Auction auction);

    void addCredits(Member member, int amount);

    boolean authMemberPay(int amount, Member member);

    boolean pay(Member member, int amount);
}
