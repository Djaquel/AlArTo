package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Article;
import fr.eni.auctionapp.bo.Auction;
import fr.eni.auctionapp.bo.Member;
import fr.eni.auctionapp.dal.ArticleDAO;
import fr.eni.auctionapp.dal.AuctionDAO;
import fr.eni.auctionapp.dal.MemberDAO;
import fr.eni.auctionapp.security.MemberUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Locale;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberDAO memberDAO;
    private ArticleDAO articleDAO;
    private AuctionDAO auctionDAO;
    private CreditService creditService;
    private EmailService emailService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberServiceImpl(MemberDAO memberDAO, ArticleDAO articleDAO, AuctionDAO auctionDAO, CreditService creditService, EmailService emailService) {
        this.memberDAO = memberDAO;
        this.articleDAO = articleDAO;
        this.auctionDAO = auctionDAO;
        this.creditService = creditService;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    @Override
    public Optional<Member> getMemberById(int memberId) {
        return memberDAO.findById(memberId);
    }


    @Override
    public void updateUser(Member member, String password, String newPassword, Authentication authentication) {
        Member authMember = getMemberFromAuthentication(authentication);
        String authPassword = authMember.getPassword();

        if (!bCryptPasswordEncoder.matches(password, authPassword)) {
            throw new AccessDeniedException("Wrong password");
        }

        String pseudo = member.getPseudo();
        if (!authMember.getPseudo().equals(pseudo)) {
            if (memberDAO.findByPseudo(pseudo).isPresent()) {
                throw new IllegalArgumentException("Pseudo already exists");
            }
        }

        if (newPassword.isBlank()) {
            member.setPassword(authPassword);
        } else {
            String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);
            member.setPassword(encodedNewPassword);
            authMember.setPassword(encodedNewPassword);
        }
        memberDAO.update(member, authentication);
    }

    @Override
    public void updatePassword(String password, int memberId){
        memberDAO.updatePasswordByMemberId(memberId, bCryptPasswordEncoder.encode(password));
    }

    @Override
    public Optional<Member> getMemberByPseudo(String pseudo) {
        return memberDAO.findByPseudo(pseudo);
    }

    @Override
    public Optional<Member> getMemberByEmail(String email) {
        return memberDAO.findByEmail(email);
    }

    @Override
    public void createMember(Locale locale, Member member) {
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        memberDAO.insert(member);
        emailService.sendWelcomeMessage(locale, member);
    }

    @Override
    public void removeMember(Authentication authentication) {
        memberDAO.supprMemberLogout(authentication);

    }

    @Override
    public Optional<Member> getAuthenticatedMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return memberDAO.findById(getMemberFromAuthentication(auth).getId());
        }
        return Optional.empty();
    }

    @Override
    public void updateAuthenticatedMember(Member member) {
        ((MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setMember(member);
    }

    @Override
    public Member getMemberFromAuthentication(Authentication authentication) {
        Object userDetails = authentication.getPrincipal();
        if (userDetails instanceof MemberUserDetails) {
            return ((MemberUserDetails) userDetails).getMember();
        }
        throw new RuntimeException("failed to get the member using the Authentication");
    }

    @Override
    public void deactivateMember(int memberId) {
        disableMember(memberId);
        memberDAO.deactivateMemberDAO(memberId);
    }

    @Override
    public void deleteMember(int memberId) {
        disableMember(memberId);
        memberDAO.nullableSellerId(memberId);
        memberDAO.nullableBuyerId(memberId);
        memberDAO.removeById(memberId);
    }

    @Override
    public void reactivateMember(int memberId) {
        memberDAO.reactivateMemberDAO(memberId);
    }

    private void disableMember(int memberId) {

        // Refound auctions on member articles
        List<Auction> auctionFromSalesToRefound = auctionDAO.getHigherCurrentAuctionsBySellerId(memberId);
        for (Auction auction : auctionFromSalesToRefound) {
            if (auction.getMember().getId() != memberId) creditService.refound(auction);
        }

        // Refound member winning auctions
        List<Auction> winningAuctionsFromMemberToRefound = auctionDAO.getWinningAuctionsByMemberId(memberId);
        for (Auction auction : winningAuctionsFromMemberToRefound) {
            creditService.refound(auction);

            Article article = auction.getArticle();
            if (article.getSeller().getId() == memberId) {
                continue;
            }

            int articleId = article.getId();
            auctionDAO.remove(auction);
            Optional<Auction> winningAuctionOpt = auctionDAO.getHigherValidAuctionByArticleId(articleId);
            // Higher valid auction is set as winning auction
            if (winningAuctionOpt.isPresent()) {
                Auction winningAuction = winningAuctionOpt.get();
                int winningAuctionAmount = winningAuction.getAmount();
                auctionDAO.removeAuctionsAboveAmountByArticleId(articleId, winningAuctionAmount);

                article.setBuyer(winningAuction.getMember());
                article.setSellPrice(winningAuctionAmount);

                creditService.pay(winningAuction.getMember(), winningAuctionAmount);
            }
            // Reset article if no valid auction
            else {
                auctionDAO.removeAuctionsAboveAmountByArticleId(articleId, article.getInitialPrice());

                article.setBuyer(article.getSeller());
                article.setSellPrice(article.getInitialPrice());
            }
            articleDAO.update(article);
        }

        // Set member articles state to CANCELLED
        articleDAO.cancelAllSellBySellerId(memberId);
        auctionDAO.removeAuctionFromCancelledArticleBySellerId(memberId);

        // Remove remaining member auctions
        auctionDAO.deleteAuction(memberId);
    }
}


