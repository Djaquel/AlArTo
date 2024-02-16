package fr.eni.auctionapp.dal;

import fr.eni.auctionapp.bo.Article;
import fr.eni.auctionapp.bo.Pageable;
import fr.eni.auctionapp.bo.Search;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ArticleDAO extends DAO<Article> {

    void callStatusUpdateProcedure();

    Pageable<Article> findAllPaginated(int pageIndex, int itemQuantity);

    Pageable<Article> findMyArticlesBySearchPaginated(Search search, int pageIndex, int itemQuantity);

    Pageable<Article> findOthersArticlesBySearchPaginated(Search search, int pageIndex, int itemQuantity);

    void doAuction(int price, int idArticle, Authentication authentication);

    Pageable<Article> findArticlesByCategoryAndNamePaginated(int categoryId, String search, int pageIndex, int itemQuantity);

    List<Article> findAllBySellerId(int id);
    List<Article> findAllByBuyerId(int id);

    List<Article> findUnstartedBySellerId(int id);

    void update(Article article);

    void deleteArticle(int id);

    void cancelAllSellBySellerId(int memberId);

    List<Article> findWinningArticlesByMemberId(int memberId);
}
