package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.*;
import fr.eni.auctionapp.dal.ArticleDAO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    MemberService memberService;
    CreditService creditService;
    ArticleDAO articleDAO;
    AuctionService auctionService;

    public ArticleServiceImpl(MemberService memberService, CreditService creditService, ArticleDAO articleDAO, AuctionService auctionService) {
        this.memberService = memberService;
        this.creditService = creditService;
        this.articleDAO = articleDAO;
        this.auctionService = auctionService;
    }

    @Override
    public Pageable<Article> getAllArticles(int pageIndex, int itemQuantity) {
        return articleDAO.findAllPaginated(pageIndex,itemQuantity);
    }

    @Override
    public Optional<Article> getArticleById(int articleId) {
        return articleDAO.findById(articleId);
    }


    @Override
    public int insert(Article article) {
        return this.articleDAO.insert(article);
    }

    @Override
    public Pageable<Article> getFilteredArticles(Search search, Authentication authentication, int pageIndex, int itemQuantity) {
        if (authentication == null) {
            return articleDAO.findArticlesByCategoryAndNamePaginated(search.getCategoryId(), search.getSearchText(), pageIndex, itemQuantity);
        } else {
            search.setConnectedMember(memberService.getMemberFromAuthentication(authentication));
            if (search.isOwnArticles()) {
                return articleDAO.findMyArticlesBySearchPaginated(search, pageIndex, itemQuantity);
            } else {
                return articleDAO.findOthersArticlesBySearchPaginated(search, pageIndex, itemQuantity);
            }
        }
    }

    @Override
    public void updateAuctionStatus() {
        articleDAO.callStatusUpdateProcedure();
    }

    @Override
    public boolean doAuction(int price, int articleId, Authentication authentication) {
        Optional<Auction> lastHigherAuction = auctionService.getHigherAuction(articleId);
        Member member = memberService.getMemberFromAuthentication(authentication);

        boolean memberIsWinning = lastHigherAuction.map(auction -> auction.getMember().equals(member)).orElse(false);
        int debit = memberIsWinning ? price - lastHigherAuction.get().getAmount() : price;

        if (creditService.authMemberPay(debit, member)) {
            articleDAO.doAuction(price, articleId, authentication);
            if (!memberIsWinning) {
                lastHigherAuction.ifPresent(auction -> creditService.refound(auction));
            }
            auctionService.doAuction(authentication, articleId, price);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Article> getAllMyArticles(int id) {
        return this.articleDAO.findUnstartedBySellerId(id);
    }

    @Override
    public void update(Article article) {
        articleDAO.update(article);

    }

    @Override
    public void deleteArticle(int id) {
        articleDAO.deleteArticle(id);
    }

	
}

