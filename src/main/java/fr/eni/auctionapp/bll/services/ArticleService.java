package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Article;

import fr.eni.auctionapp.bo.Pageable;
import fr.eni.auctionapp.bo.Search;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    Pageable<Article> getAllArticles(int pageIndex, int itemQuantity);

    Optional<Article> getArticleById(int articleId);


    int insert(Article article);

    Pageable<Article> getFilteredArticles(Search search, Authentication authentication, int pageIndex, int itemQuantity);

    void updateAuctionStatus();

    boolean doAuction(int price, int idArticle, Authentication authentication);

    List<Article> getAllMyArticles(int id);

    void update(Article article);

    void deleteArticle(int id);



}
