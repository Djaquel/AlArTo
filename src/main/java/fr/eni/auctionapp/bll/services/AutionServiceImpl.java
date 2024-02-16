package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Article;
import fr.eni.auctionapp.bo.Auction;
import fr.eni.auctionapp.dal.ArticleDAO;
import fr.eni.auctionapp.dal.AuctionDAO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutionServiceImpl implements AuctionService {

    private AuctionDAO auctionDAO;
    private ArticleDAO articleDAO;

    public AutionServiceImpl(AuctionDAO auctionDAO, ArticleDAO articleDAO) {
        this.auctionDAO = auctionDAO;
        this.articleDAO = articleDAO;
    }

    @Override
    public List<Auction> getAllAuctions() {
        return null;
    }

    @Override
    public Optional<Auction> getAuctionById(int auctionId) {
        return null;
    }

    @Override
    public Optional<Auction> getHigherAuction(int articleId) {
        return auctionDAO.getHigherAuction(articleId);
    }

    @Override
    public boolean checkPriceHigher(int price, int idArticle) {
        Article article = articleDAO.findById(idArticle).get();
        return price > article.getSellPrice();
    }

    @Override
    public void doAuction(Authentication authentication, int idArticle, int price) {
        auctionDAO.doAuction(authentication, idArticle, price);
    }

	@Override
	public List<Auction> getAuctionsByArticleId(int articleId) {
        return auctionDAO.findAllByArticleId(articleId);
	}
}
