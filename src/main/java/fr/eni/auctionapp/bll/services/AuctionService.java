package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Auction;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface AuctionService {
    List<Auction> getAllAuctions();

    Optional<Auction> getAuctionById(int auctionId);

    Optional<Auction> getHigherAuction(int articleId);

    boolean checkPriceHigher(int price, int idArticle);

    void doAuction(Authentication authentication, int idArticle, int price);

	List<Auction> getAuctionsByArticleId(int articleId);
}
