package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Auction;
import fr.eni.auctionapp.bo.Member;
import fr.eni.auctionapp.dal.MemberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CreditServiceImpl implements CreditService {

    MemberDAO memberDAO;

    public CreditServiceImpl(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public boolean hasEnough(Member member, int amount) {
        return member.getCredits() >= amount;
    }

    @Override
    public void refound(Auction auction) {
        Member member = auction.getMember();
        updateMemberCredits(member, member.getCredits() + auction.getAmount());
    }

    @Override
    public void addCredits(Member member, int amount) {
        updateMemberCredits(member, member.getCredits() + amount);
    }

    @Override
    public boolean authMemberPay(int amount, Member member) {
        return pay(member, amount);
    }

    @Override
    public boolean pay(Member member, int amount) {
        if (!hasEnough(member, amount)) {
            return false;
        }
        updateMemberCredits(member, member.getCredits() - amount);
        return true;
    }

    private void updateMemberCredits(Member member, int amount) {
        memberDAO.updateCredits(member, amount);
        member.setCredits(amount);
    }
}
