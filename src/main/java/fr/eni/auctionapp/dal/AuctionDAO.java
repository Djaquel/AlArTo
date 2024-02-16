package fr.eni.auctionapp.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;

import fr.eni.auctionapp.bo.Auction;

public interface AuctionDAO extends DAO<Auction> {

    void doAuction(Authentication authentication, int idArticle, int price);

    void deleteAuction(int memberId);

    Optional<Auction> getHigherAuction(int articleId);

	List<Auction> findAllByArticleId(int articleId);

    List<Auction> getHigherCurrentAuctionsBySellerId(int memberId);

    List<Auction> getWinningAuctionsByMemberId(int memberId);

    Optional<Auction> getHigherValidAuctionByArticleId(int articleId);

    void removeAuctionsAboveAmountByArticleId(int articleId, int amount);

    void removeAuctionFromCancelledArticleBySellerId(int memberId);
}
